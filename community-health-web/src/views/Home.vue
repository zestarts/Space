<template>
  <div class="home">
    <el-card>
      <h3>欢迎回来，{{ userStore.user?.realName || userStore.user?.username }}</h3>
      <p>角色：{{ userStore.user?.roleName || userStore.user?.roleCode }}</p>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()
</script>

<style scoped>
.home { padding: 20px; }
</style>