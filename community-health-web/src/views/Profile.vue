<template>
  <div class="profile">
    <el-card v-loading="loading">
      <template #header><h3>个人中心</h3></template>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="真实姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="角色">{{ form.roleName || form.roleCode }}</el-form-item>
        <el-form-item><el-button type="primary" @click="handleSave">保存</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const form = reactive({ username: '', phone: '', realName: '', roleCode: '', roleName: '' })

onMounted(async () => {
  loading.value = true
  try {
    const data: any = await getProfile()
    Object.assign(form, data)
  } finally {
    loading.value = false
  }
})

async function handleSave() {
  loading.value = true
  try {
    await updateProfile({ phone: form.phone, realName: form.realName })
    ElMessage.success('保存成功')
    userStore.fetchProfile()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile { padding: 20px; max-width: 600px; }
</style>