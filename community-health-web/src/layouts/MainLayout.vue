<template>
  <el-container class="layout">
    <el-header class="header">
      <div class="header-left">
        <span class="title">社区健康守护系统</span>
        <el-menu
          v-if="isAdmin"
          mode="horizontal"
          :ellipsis="false"
          class="nav-menu"
          :default-active="route.path"
          @select="(key: string) => router.push(key)"
        >
          <el-menu-item index="/admin/users">用户管理</el-menu-item>
        </el-menu>
      </div>
      <el-dropdown @command="handleCommand">
        <span class="user-info">{{ userStore.user?.realName || userStore.user?.username }}</span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-if="isAdmin" command="adminUsers">用户管理</el-dropdown-item>
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="health">健康档案</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isAdmin = computed(() => {
  const code = userStore.user?.roleCode
  return code === 'SUPER_ADMIN' || code === 'ADMIN'
})

onMounted(() => {
  if (!userStore.user) userStore.fetchProfile()
})

function handleCommand(cmd: string) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (cmd === 'adminUsers') {
    router.push('/admin/users')
  } else {
    router.push(`/${cmd}`)
  }
}
</script>

<style scoped>
.layout { min-height: 100vh; }
.header {
  display: flex; align-items: center; justify-content: space-between;
  background: #409eff; color: #fff; padding: 0 24px;
}
.header-left { display: flex; align-items: center; gap: 24px; }
.title { font-size: 18px; font-weight: bold; white-space: nowrap; }
.user-info { cursor: pointer; }
.nav-menu { border-bottom: none; }
.nav-menu .el-menu-item {
  color: #cde4ff; border-bottom: 2px solid transparent; height: 60px; line-height: 60px;
}
.nav-menu .el-menu-item:hover { background: rgba(255,255,255,0.1); color: #fff; }
.nav-menu .el-menu-item.is-active { color: #fff; border-bottom-color: #fff; }
</style>