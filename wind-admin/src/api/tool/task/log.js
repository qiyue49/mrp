import request from '@/utils/request'

export function fetchScheduleJobLogList(query) {
  return request({
    url: '/task/schedule/job/log/list',
    method: 'get',
    params: query
  })
}

export function deleteScheduleJobLog(id) {
  return request({
    url: '/task/schedule/job/log/delete/' + id,
    method: 'post'
  })
}
export function batchDeleteScheduleJobLog(ids) {
  return request({
    url: '/task/schedule/job/log/batch/delete',
    method: 'post',
    data: { ids }
  })
}
