import request from '@/utils/request'
// 个人信息
export function fetchUserList(data) {
  return request({
    url: `/json/user/list`,
    method: 'post',
    data
  })
}
export function getUser(id) {
  return request({
    url: `/json/user/detail/${id}`,
    method: 'get'
  })
}

