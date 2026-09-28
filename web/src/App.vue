<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { api, type Diagnosis } from './api'
import { useSessionStore } from './stores/session'

const session = useSessionStore()
const circuitId = ref('')
const loading = ref(false)
const history = ref<Diagnosis[]>([])
const selected = ref<Diagnosis | null>(null)

async function search() {
  if (!circuitId.value.trim()) return ElMessage.warning('请输入 circuitId')
  loading.value = true
  try {
    const { data } = await api.get<Diagnosis[]>('/api/diagnoses', { params: { circuitId: circuitId.value.trim() } })
    history.value = data
    selected.value = data[0] || null
  } catch { ElMessage.error('查询失败，请检查平台 API 和 MySQL') }
  finally { loading.value = false }
}

async function create() {
  if (!circuitId.value.trim()) return ElMessage.warning('请输入 circuitId')
  loading.value = true
  try {
    const { data } = await api.post<{ diagnosisId: number }>('/api/diagnoses', { circuitId: circuitId.value.trim() })
    const result = await api.get<Diagnosis>(`/api/diagnoses/${data.diagnosisId}`)
    selected.value = result.data
    // Keep the last circuit in the URL so browser refresh can restore history.
    window.history.replaceState(null, '', `?circuitId=${encodeURIComponent(circuitId.value.trim())}`)
    await search()
    ElMessage.success('诊断记录已创建')
  } catch { ElMessage.error('创建失败，请检查平台 API 和 MySQL') }
  finally { loading.value = false }
}

onMounted(async () => {
  try { await session.load() } catch { ElMessage.error('无法获取登录态，请检查平台 API') }
  circuitId.value = new URLSearchParams(location.search).get('circuitId') || ''
  if (circuitId.value) await search()
})
</script>

<template>
  <el-container class="layout">
    <el-header class="header"><strong>企业统一平台</strong><span>网络诊断 · 当前用户：{{ session.username || '连接中' }}</span></el-header>
    <el-main class="main">
      <h1>线路诊断</h1>
      <p class="intro">输入 circuitId 创建诊断记录。当前 SW / PE 为待接入占位分支。</p>
      <el-card>
        <div class="actions">
          <el-input v-model="circuitId" placeholder="请输入 circuitId" clearable @keyup.enter="search" />
          <el-button type="primary" :loading="loading" @click="create">发起诊断</el-button>
          <el-button :loading="loading" @click="search">查询历史</el-button>
        </div>
      </el-card>
      <el-row :gutter="20" class="results">
        <el-col :xs="24" :md="10"><el-card><template #header>历史记录</template>
          <el-empty v-if="!history.length" description="暂无记录" />
          <div v-for="item in history" :key="item.diagnosisId" class="history-item" @click="selected = item">
            <strong>#{{ item.diagnosisId }}</strong><span>{{ item.status }}</span><small>{{ item.createdAt }}</small>
          </div>
        </el-card></el-col>
        <el-col :xs="24" :md="14"><el-card><template #header>诊断结果</template>
          <el-empty v-if="!selected" description="请选择或创建诊断记录" />
          <template v-else>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="诊断 ID">{{ selected.diagnosisId }}</el-descriptions-item>
              <el-descriptions-item label="caseId">{{ selected.caseId }}</el-descriptions-item>
              <el-descriptions-item label="circuitId">{{ selected.circuitId }}</el-descriptions-item>
              <el-descriptions-item label="状态">{{ selected.status }}</el-descriptions-item>
            </el-descriptions>
            <h3>分支步骤</h3>
            <el-table :data="selected.steps" border><el-table-column prop="branch" label="分支" width="90" /><el-table-column prop="systemName" label="系统" /><el-table-column prop="status" label="状态" width="100" /><el-table-column prop="stepName" label="说明" /></el-table>
          </template>
        </el-card></el-col>
      </el-row>
    </el-main>
  </el-container>
</template>
