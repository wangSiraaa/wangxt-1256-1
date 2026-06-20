import request from './common'
import type { PageResult, PageQuery } from './common'

export interface AppealSubmitDTO {
  restrictionId: number
  driverId: number
  appealReason: string
  restProofUrl?: string
  restProofDesc?: string
  restStartTime?: string
  restEndTime?: string
  restMinutes?: number
}

export interface MaterialCheckDTO {
  appealId: number
  materialComplete: number
  incompleteReason?: string
  auditorId?: number
  auditRemark?: string
}

export interface AppealQueryDTO extends PageQuery {
  driverId?: number
  restrictionId?: number
  appealStatus?: number
  materialComplete?: number
  appealNo?: string
}

export interface DriverAppealVO {
  id: number
  restrictionId: number
  driverId: number
  appealNo: string
  appealReason: string
  restProofUrl?: string
  restProofDesc?: string
  restStartTime?: string
  restEndTime?: string
  restMinutes?: number
  materialComplete: number
  incompleteReason?: string
  appealStatus: number
  appealStatusDesc: string
  submitTime: string
  auditorId?: number
  auditTime?: string
  auditRemark?: string
  createTime: string
  driverCode?: string
  driverName?: string
  driverPhone?: string
  restrictionNo?: string
}

export function submitAppeal(dto: AppealSubmitDTO) {
  return request<AppealSubmitDTO, DriverAppealVO>('/api/appeal/submit', 'post', dto)
}

export function materialCheck(dto: MaterialCheckDTO) {
  return request<MaterialCheckDTO, DriverAppealVO>('/api/appeal/material-check', 'post', dto)
}

export function appealPage(query: AppealQueryDTO) {
  return request<AppealQueryDTO, PageResult<DriverAppealVO>>('/api/appeal/page', 'post', query)
}

export function appealDetail(id: number) {
  return request<any, DriverAppealVO>(`/api/appeal/${id}`, 'get')
}

export function listAppealsByDriver(driverId: number) {
  return request<any, DriverAppealVO[]>(`/api/appeal/driver/${driverId}`, 'get')
}
