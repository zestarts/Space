<template>
  <div class="admin-users">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>用户管理</h3>
          <el-input
            v-model="keyword"
            placeholder="搜索用户名/手机号/姓名"
            clearable
            style="width: 260px"
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="realName" label="真实姓名" min-width="100" />
        <el-table-column prop="roleName" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="row.roleCode === 'SUPER_ADMIN' ? 'danger' : row.roleCode === 'ADMIN' ? 'warning' : ''">
              {{ row.roleName || row.roleCode }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === 1"
              :loading="togglingId === row.id"
              :disabled="row.roleCode === 'SUPER_ADMIN' && userStore.user?.roleCode !== 'SUPER_ADMIN'"
              @change="(val: boolean) => handleToggle(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" min-width="170" />
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          :page-size="size"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="fetchList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUsers, updateUserStatus } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const list = ref<any[]>([])
const page = ref(1)
const size = ref(15)
const total = ref(0)
const keyword = ref('')
const togglingId = ref<number | null>(null)

onMounted(() => {
  fetchList()
})

function handleSearch() {
  page.value = 1
  fetchList()
}

async function fetchList() {
  loading.value = true
  try {
    const data: any = await listUsers({ page: page.value, size: size.value, keyword: keyword.value })
    list.value = data.records || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

async function handleToggle(row: any, val: boolean) {
  const action = val ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确认${action}用户「${row.username}」？`, '操作确认')
  } catch {
    return
  }
  togglingId.value = row.id
  try {
    await updateUserStatus(row.id, val ? 1 : 0)
    row.status = val ? 1 : 0
    ElMessage.success(`${action}成功`)
  } finally {
    togglingId.value = null
  }
}
</script>

<style scoped>
.admin-users { padding: 20px; }
.card-header { display: flex; align-items: center; justify-content: space-between; }
.card-header h3 { margin: 0; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>