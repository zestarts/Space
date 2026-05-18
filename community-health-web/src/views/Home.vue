<template>
  <div class="home-page">
    <div class="welcome-card">
      <div class="welcome-title">欢迎回来，{{ userStore.user?.realName || userStore.user?.username }}</div>
      <div class="welcome-sub">
        <el-tag :type="roleTagType">{{ userStore.user?.roleName }}</el-tag>
        <span class="date">{{ today }}</span>
      </div>
    </div>

    <div class="quick-cards">
      <div class="feature-card primary" @click="router.push('/health/daily')">
        <div class="card-icon">💓</div>
        <div class="card-title">今日健康指数</div>
        <div class="card-desc">
          {{ dailyDone ? '今日已记录 ✓' : '记录血压、血糖、心率等' }}
        </div>
        <el-button :type="dailyDone ? 'default' : 'primary'" size="small">
          {{ dailyDone ? '查看记录' : '立即记录' }}
        </el-button>
      </div>

      <div class="feature-card" @click="router.push('/health')">
        <div class="card-icon">📋</div>
        <div class="card-title">健康档案</div>
        <div class="card-desc">管理您的长期健康信息</div>
        <el-button type="default" size="small">查看档案</el-button>
      </div>

      <div class="feature-card" @click="router.push('/profile')">
        <div class="card-icon">👤</div>
        <div class="card-title">个人中心</div>
        <div class="card-desc">管理个人信息与偏好设置</div>
        <el-button type="default" size="small">进入设置</el-button>
      </div>
    </div>

    <el-row v-if="todayRecord" :gutter="20" class="today-summary">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>今日概览</template>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="血压">
              <span :class="{ 'text-danger': bpWarning }">
                {{ todayRecord.bloodPressureSystolic }}/{{ todayRecord.bloodPressureDiastolic }} mmHg
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="血糖">{{ todayRecord.bloodSugar }} mmol/L</el-descriptions-item>
            <el-descriptions-item label="心率">{{ todayRecord.heartRate }} bpm</el-descriptions-item>
            <el-descriptions-item label="体重">{{ todayRecord.weight }} kg</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>生活状态</template>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="睡眠">{{ todayRecord.sleepHours }}h ({{ todayRecord.sleepQuality }})</el-descriptions-item>
            <el-descriptions-item label="运动">{{ todayRecord.exerciseMinutes }}分钟</el-descriptions-item>
            <el-descriptions-item label="饮食">{{ todayRecord.dietQuality }}</el-descriptions-item>
            <el-descriptions-item label="心情">{{ todayRecord.mood }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getTodayRecord } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const today = computed(() => {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
})

const roleTagType = computed(() => {
  switch (userStore.user?.roleCode) {
    case 'SUPER_ADMIN': return 'danger'
    case 'ADMIN': return 'warning'
    default: return 'info'
  }
})

const todayRecord = ref<any>(null)
const dailyDone = ref(false)

const bpWarning = computed(() => {
  if (!todayRecord.value) return false
  const s = todayRecord.value.bloodPressureSystolic
  const d = todayRecord.value.bloodPressureDiastolic
  return s >= 140 || d >= 90 || s <= 90 || d <= 60
})

onMounted(async () => {
  try {
    const data: any = await getTodayRecord()
    if (data && data.recordDate) {
      todayRecord.value = data
      dailyDone.value = true
    }
  } catch { /* 今日未记录 */ }
})
</script>

<style scoped>
.home-page { padding: 20px; max-width: 960px; margin: 0 auto; }

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff; padding: 32px; border-radius: 12px; margin-bottom: 24px;
}
.welcome-title { font-size: 24px; font-weight: bold; }
.welcome-sub { display: flex; align-items: center; gap: 12px; margin-top: 8px; }
.date { opacity: .8; }

.quick-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-bottom: 24px; }

.feature-card {
  background: #fff; border-radius: 12px; padding: 24px;
  text-align: center; cursor: pointer; transition: all .2s;
  border: 2px solid #e5e7eb;
}
.feature-card:hover { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(0,0,0,.1); }
.feature-card.primary { border-color: #409eff; }

.card-icon { font-size: 36px; margin-bottom: 8px; }
.card-title { font-size: 16px; font-weight: bold; margin-bottom: 4px; }
.card-desc { font-size: 13px; color: #6b7280; margin-bottom: 12px; }

.text-danger { color: #f56c6c; font-weight: bold; }
</style>