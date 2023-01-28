import request from '@/utils/request'

export function fetchDictMap () {
  return request({
    url: '/json/dict',
    method: 'get'
  })
}
