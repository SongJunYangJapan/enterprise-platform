import axios from 'axios'

// Set VITE_API_BASE_URL for separate deployment. Vite proxies /api locally.
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
