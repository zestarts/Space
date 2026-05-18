<template>
  <div class="health-profile">
    <el-card v-loading="loading">
      <template #header><h3>健康档案</h3></template>
      <el-form :model="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄"><el-input-number v-model="form.age" :min="0" :max="150" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" placeholder="请选择">
                <el-option label="男" value="male" />
                <el-option label="女" value="female" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="职业">
          <el-select v-model="form.occupation" placeholder="请选择">
            <el-option label="久坐办公族" value="sedentary_office" />
            <el-option label="体力劳动者" value="manual_labor" />
            <el-option label="轮班族" value="shift_worker" />
            <el-option label="退休" value="retired" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="居住情况">
          <el-select v-model="form.livingCondition" placeholder="请选择">
            <el-option label="独居" value="alone" />
            <el-option label="与家人同住" value="with_family" />
          </el-select>
        </el-form-item>
        <el-form-item label="残疾评级"><el-input v-model="form.disabilityLevel" placeholder="如无请留空" /></el-form-item>
        <el-form-item label="慢性病"><el-input v-model="form.chronicDiseases" placeholder="多个用逗号分隔" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="血压"><el-input v-model="form.bloodPressure" placeholder="120/80" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血糖"><el-input v-model="form.bloodSugar" placeholder="5.6" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="心率"><el-input-number v-model="form.heartRate" :min="30" :max="250" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身高(cm)"><el-input v-model="form.height" placeholder="170" /></el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重(kg)"><el-input v-model="form.weight" placeholder="65" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item><el-button type="primary" @click="handleSave">保存健康档案</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getHealthProfile, updateHealthProfile } from '@/api/user'

const loading = ref(false)
const form = reactive<any>({
  age: null, gender: '', occupation: '', livingCondition: '',
  disabilityLevel: '', chronicDiseases: '', bloodPressure: '',
  bloodSugar: null, heartRate: null, height: null, weight: null
})

onMounted(async () => {
  loading.value = true
  try {
    const data: any = await getHealthProfile()
    if (data) Object.assign(form, data)
  } catch {
    // profile may not exist yet, use defaults
  } finally {
    loading.value = false
  }
})

async function handleSave() {
  loading.value = true
  try {
    await updateHealthProfile(form)
    ElMessage.success('健康档案保存成功')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.health-profile { padding: 20px; max-width: 900px; }
</style>