import request from './common'
import type { PageResult, PageQuery } from './common'

export interface OnlineHoursImportDTO {
  driverId: number
  workDate: string
  onlineStart: string
  onlineEnd: string
  onlineMinutes?: number
  workMinutes?: number
  restMinutes?: number
  orderCount?: number
  batchNo?: string
}

export interface DriverOnlineHours {
  id: number
  driverId: number
  workDate: string
  onlineStart: string
  onlineEnd: string
  onlineMinutes: number
  workMinutes: number
  restMinutes: number
  orderCount: number
}

export interface RestrictionQueryDTO extends PageQuery {
  driverId?: number
  restrictionType?: number
  restrictionStatus?: number
  riskLevel?: number
  restrictionNo?: string
}

export interface RiskRestrictionVO {
  id: number
  driverId: number
  restrictionNo: string
  restrictionType: number
  restrictionTypeDesc: string
  triggerReason: string
  triggerValue: number
  thresholdValue: number
  riskLevel: number
  riskLevelDesc: string
  restrictionStatus: number
  restrictionStatusDesc: string
  startTime: string
  endTime?: string
  appealId?: number
  reviewId?: number
  evidenceData?: string
  createTime: string
  driverCode?: string
  driverName?: string
  driverPhone?: string
}

export function importHours(dto: OnlineHoursImportDTO) {
  return request<OnlineHoursImportDTO, DriverOnlineHours>('/api/risk/hours/import', 'post', dto)
}

export function batchImportHours(dtoList: OnlineHoursImportDTO[]) {
  return request<OnlineHoursImportDTO[], DriverOnlineHours[]>('/api/risk/hours/batch-import', 'post', dtoList)
}

export function listHours(driverId: number, start: string, end: string) {
  return request<any, DriverOnlineHours[]>(
    `/api/risk/hours/list?driverId=${driverId}&start=${start}&end=${end}`,
    'get'
  )
}

export function restrictionPage(query: RestrictionQueryDTO) {
  return request<RestrictionQueryDTO, PageResult<RiskRestrictionVO>>(
    '/api/risk/restriction/page',
    'post',
    query
  )
}

export function restrictionDetail(id: number) {
  return request<any, RiskRestrictionVO>(`/api/risk/restriction/${id}`, 'get')
}

export function activeRestrictions(driverId: number) {
  return request<any, RiskRestrictionVO[]>(`/api/risk/restriction/active/${driverId}`, 'get')
}

export function historyRestrictions(driverId: number) {
  return request<any, RiskRestrictionVO[]>(`/api/risk/restriction/history/${driverId}`, 'get')
}
