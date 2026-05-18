#!/usr/bin/env python3
import http.server
import subprocess
import os
import sys
import urllib.parse
import time
import threading

ALIYUN_MIRRORS = [
    "https://maven.aliyun.com/repository/public",
    "https://maven.aliyun.com/repository/central",
]

LOCAL_REPO = os.path.expanduser("~/.m2/repository")
_last_request_time = 0.0
_lock = threading.Lock()

class Handler(http.server.BaseHTTPRequestHandler):
    def do_HEAD(self):
        self.do_GET()

    def do_GET(self):
        global _last_request_time
        path = urllib.parse.unquote(self.path)
        local_path = os.path.join(LOCAL_REPO, path.lstrip("/"))

        if os.path.exists(local_path) and os.path.getsize(local_path) > 0:
            self.send_response(200)
            self.end_headers()
            if self.command == "GET":
                with open(local_path, "rb") as f:
                    self.wfile.write(f.read())
            return

        os.makedirs(os.path.dirname(local_path), exist_ok=True)

        mirror_idx = hash(path) % len(ALIYUN_MIRRORS)
        url = ALIYUN_MIRRORS[mirror_idx] + path

        with _lock:
            elapsed = time.time() - _last_request_time
            if elapsed < 0.5:
                time.sleep(0.5 - elapsed)
            _last_request_time = time.time()

        for attempt in range(3):
            result = subprocess.run(
                ["curl", "-sL", "-o", local_path, "--connect-timeout", "30", "--max-time", "120", url],
                capture_output=True
            )

            if os.path.exists(local_path) and os.path.getsize(local_path) > 0:
                with open(local_path, "rb") as f:
                    content = f.read(200)
                if b"exceed" in content.lower() or b"rate limit" in content.lower():
                    os.remove(local_path)
                    time.sleep(2)
                    continue
                break
            time.sleep(1)
        else:
            if os.path.exists(local_path):
                os.remove(local_path)
            self.send_response(404)
            self.end_headers()
            return

        self.send_response(200)
        self.end_headers()
        if self.command == "GET":
            with open(local_path, "rb") as f:
                self.wfile.write(f.read())

    def log_message(self, format, *args):
        path = str(args[0]) if args else ""
        sys.stderr.write("[proxy] %s\n" % path)

class ThreadedHTTPServer(http.server.ThreadingHTTPServer):
    allow_reuse_address = True
    daemon_threads = True

server = ThreadedHTTPServer(("127.0.0.1", 8082), Handler)
print("Maven proxy ready on http://127.0.0.1:8082", flush=True)
server.serve_forever()