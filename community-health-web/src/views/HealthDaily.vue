<template>
  <div class="health-daily-page">
    <el-card>
      <template #header>
        <div class="page-header">
          <h3>今日健康指数</h3>
          <span class="record-date">{{ record.recordDate || today }}</span>
        </div>
      </template>

      <el-form ref="formRef" :model="record" label-width="100px" label-position="top">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="收缩压 (mmHg)">
              <el-input-number v-model="record.bloodPressureSystolic" :min="60" :max="250" placeholder="120" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="舒张压 (mmHg)">
              <el-input-number v-model="record.bloodPressureDiastolic" :min="30" :max="150" placeholder="80" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="心率 (bpm)">
              <el-input-number v-model="record.heartRate" :min="30" :max="250" placeholder="72" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="空腹血糖 (mmol/L)">
              <el-input-number v-model="record.bloodSugar" :precision="1" :step="0.1" :min="1.0" :max="30.0" placeholder="5.3" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="体重 (kg)">
              <el-input-number v-model="record.weight" :precision="1" :min="30" :max="200" placeholder="70" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="运动时长 (分钟)">
              <el-input-number v-model="record.exerciseMinutes" :min="0" :max="600" placeholder="30" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="睡眠时长 (小时)">
              <el-input-number v-model="record.sleepHours" :precision="1" :step="0.5" :min="0" :max="24" placeholder="7.5" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="睡眠质量">
              <el-select v-model="record.sleepQuality" placeholder="请选择" clearable>
                <el-option label="优" value="优" />
                <el-option label="良" value="良" />
                <el-option label="差" value="差" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="饮食质量">
              <el-select v-model="record.dietQuality" placeholder="请选择" clearable>
                <el-option label="优" value="优" />
                <el-option label="良" value="良" />
                <el-option label="差" value="差" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="今日心情">
              <el-select v-model="record.mood" placeholder="请选择" clearable>
                <el-option label="😊 愉悦" value="愉悦" />
                <el-option label="😐 平静" value="平静" />
                <el-option label="😟 焦虑" value="焦虑" />
                <el-option label="😞 低落" value="低落" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="今日症状">
              <el-input v-model="record.symptoms" placeholder="如：头晕、乏力、胸闷" maxlength="500" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input v-model="record.notes" type="textarea" :rows="2" placeholder="其他需要记录的信息..." maxlength="500" show-word-limit />
        </el-form-item>

        <div class="form-actions">
          <el-button @click="router.back()">返回</el-button>
          <el-button type="primary" :loading="saving" @click="handleSave">保存记录</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTodayRecord, saveDailyRecord } from '@/api/user'

const router = useRouter()
const saving = ref(false)

const today = new Date().toISOString().split('T')[0]

const record = reactive<any>({
  recordDate: today,
  bloodPressureSystolic: null, bloodPressureDiastolic: null, heartRate: null,
  bloodSugar: null, weight: null, exerciseMinutes: null,
  sleepHours: null, sleepQuality: '', dietQuality: '',
  mood: '', symptoms: '', notes: ''
})

onMounted(async () => {
  try {
    const data: any = await getTodayRecord()
    if (data && data.recordDate) {
      Object.keys(record).forEach(k => {
        if (k !== 'recordDate' && data[k] != null) {
          ;(record as any)[k] = data[k]
        }
      })
    }
  } catch { /* 无今日记录 */ }
})

async function handleSave() {
  saving.value = true
  try {
    await saveDailyRecord({ ...record })
    ElMessage.success('健康数据保存成功')
    router.push('/')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.health-daily-page { padding: 20px; max-width: 1024px; margin: 0 auto; }
.page-header { display: flex; align-items: center; justify-content: space-between; }
.page-header h3 { margin: 0; }
.record-date { color: #909399; font-size: 14px; }
.form-actions { display: flex; justify-content: flex-end; gap: 12px; padding-top: 20px; border-top: 1px solid #ebeef5; }
</style>