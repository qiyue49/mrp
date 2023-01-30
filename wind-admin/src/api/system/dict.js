import request from '@/utils/request'

export function fetchDictMap () {
  return request({
    url: '/sys/dict',
    method: 'get'
  })
}
