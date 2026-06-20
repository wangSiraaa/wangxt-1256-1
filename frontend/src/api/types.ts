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
