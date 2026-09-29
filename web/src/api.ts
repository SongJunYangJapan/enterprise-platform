import axios from 'axios'

// 前后端分开部署时，设置环境变量 VITE_API_BASE_URL 指向后端地址。
// 本地开发不用设：Vite 会自动把 /api 请求代理到后端（见 vite.config.ts）。
export const api = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL || '' })

export interface Step {
  id: number
  branch: 'SW' | 'PE'
  systemName: string
  stepName: string
  status: string
  rawOutput: string | null
  errorMessage: string | null
}
export interface Diagnosis {
  diagnosisId: number
  caseId: string
  circuitId: string
  status: string
  createdBy: string
  createdAt: string
  steps: Step[]
}
