import request from '@/utils/request'

export function fetchOperationLogList(query) {
  return request({
    url: '/monitor/operation/log/list',
    method: 'get',
    params: query
  })
}

export function deleteOperationLog(ids) {
  return request({
    url: '/monitor/operation/log/delete',
    method: 'post',
    data: { ids }
  })
}
