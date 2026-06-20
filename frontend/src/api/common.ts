import axiosService from '@/utils/request'

export interface PageResult<T> {
  total: number
  records: T[]
}

export interface PageQuery {
  pageNum: number
  pageSize: number
}

export interface ThresholdConfig {
  maxConsecutiveOrders: number
  maxOnlineHours: number
  consecutiveHoursThreshold: number
  requireRestMinutes: number
}

type Method = 'get' | 'post' | 'put' | 'delete'

export default function request<T, R>(url: string, method: Method = 'get', data?: T, headers?: Record<string, string>): Promise<R> {
  return axiosService.request({
    url,
    method,
    data: method === 'get' ? undefined : data,
    params: method === 'get' ? data : undefined,
    headers
  }) as Promise<R>
}

export function getThreshold() {
  return request<any, ThresholdConfig>('/api/common/threshold')
}

function uploadFile(file: File): Promise<{ url: string; name: string; originalName: string }> {
  const formData = new FormData()
  formData.append('file', file)
  return request<FormData, { url: string; name: string; originalName: string }>(
    '/api/common/upload',
    'post',
    formData as any,
    { 'Content-Type': 'multipart/form-data' }
  )
}

export { type PageResult, type PageQuery, type ThresholdConfig }
export { getThreshold, uploadFile }
