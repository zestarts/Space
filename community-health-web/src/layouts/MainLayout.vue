<template>
  <el-container class="layout">
    <el-header class="header">
      <span class="title">社区健康守护系统</span>
      <el-dropdown @command="handleCommand">
        <span class="user-info">{{ userStore.user?.realName || userStore.user?.username }}</span>
        <template #dropdown>
          <el-dropdown-menu>
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
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

onMounted(() => {
  if (!userStore.user) userStore.fetchProfile()
})

function handleCommand(cmd: string) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
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
.title { font-size: 18px; font-weight: bold; }
.user-info { cursor: pointer; }
</style>