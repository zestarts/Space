<template>
  <div class="profile-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="个人信息" name="info">
        <el-card v-loading="infoLoading">
          <template #header><h3 style="margin:0">基本信息</h3></template>
          <el-form :model="form" label-width="100px">
            <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
            <el-form-item label="手机号"><el-input v-model="form.phone" placeholder="请输入手机号" /></el-form-item>
            <el-form-item label="真实姓名"><el-input v-model="form.realName" placeholder="请输入姓名" /></el-form-item>
            <el-form-item label="角色">
              <el-tag>{{ form.roleName || form.roleCode }}</el-tag>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="infoSaving" @click="saveInfo">保存信息</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="偏好设置" name="preferences">
        <el-card>
          <template #header><h3 style="margin:0">预警偏好</h3></template>
          <el-form label-width="140px">
            <el-form-item label="预警通知">
              <el-switch v-model="prefs.alertEnabled" active-text="开启" inactive-text="关闭" />
              <span class="form-hint">关闭后将不会收到健康异常提醒</span>
            </el-form-item>
            <el-divider />
            <el-form-item label="血压上限 (收缩压)">
              <el-input-number v-model="prefs.bpSystolicHigh" :min="100" :max="200" /> mmHg
              <span class="form-hint">超过此值触发预警</span>
            </el-form-item>
            <el-form-item label="血压上限 (舒张压)">
              <el-input-number v-model="prefs.bpDiastolicHigh" :min="60" :max="130" /> mmHg
            </el-form-item>
            <el-form-item label="血压下限 (收缩压)">
              <el-input-number v-model="prefs.bpSystolicLow" :min="60" :max="120" /> mmHg
            </el-form-item>
            <el-form-item label="血糖上限">
              <el-input-number v-model="prefs.bloodSugarHigh" :min="4.0" :max="20.0" :precision="1" /> mmol/L
            </el-form-item>
            <el-form-item label="心率上限">
              <el-input-number v-model="prefs.heartRateHigh" :min="60" :max="200" /> bpm
            </el-form-item>
            <el-form-item label="心率下限">
              <el-input-number v-model="prefs.heartRateLow" :min="30" :max="80" /> bpm
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="savePrefs">保存偏好</el-button>
              <el-button @click="resetPrefs">恢复默认</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const activeTab = ref('info')
const infoLoading = ref(false)
const infoSaving = ref(false)

const form = reactive({ username: '', phone: '', realName: '', roleCode: '', roleName: '' })

const defaults = {
  alertEnabled: true,
  bpSystolicHigh: 140, bpDiastolicHigh: 90,
  bpSystolicLow: 90, bpDiastolicLow: 60,
  bloodSugarHigh: 7.0, heartRateHigh: 100, heartRateLow: 50
}

const prefs = reactive({ ...defaults })

onMounted(async () => {
  infoLoading.value = true
  try {
    const data: any = await getProfile()
    Object.assign(form, data)
  } finally {
    infoLoading.value = false
  }
  const saved = localStorage.getItem('health_prefs')
  if (saved) Object.assign(prefs, JSON.parse(saved))
})

async function saveInfo() {
  infoSaving.value = true
  try {
    await updateProfile({ phone: form.phone, realName: form.realName })
    ElMessage.success('保存成功')
    userStore.fetchProfile()
  } finally {
    infoSaving.value = false
  }
}

function savePrefs() {
  localStorage.setItem('health_prefs', JSON.stringify(prefs))
  ElMessage.success('偏好已保存')
}

function resetPrefs() {
  Object.assign(prefs, defaults)
  localStorage.removeItem('health_prefs')
  ElMessage.success('已恢复默认偏好')
}
</script>

<style scoped>
.profile-page { padding: 20px; max-width: 700px; margin: 0 auto; }
.form-hint { margin-left: 8px; color: #909399; font-size: 13px; }
</style>