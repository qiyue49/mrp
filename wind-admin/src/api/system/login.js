import request from '@/utils/request'

/**
 * 用户登录
 */
export function userLogin(data) {
  return request({
    url: '/json/user/login',
    method: 'post',
    data
  })
}
// 修改密码
export function changePassword(data) {
  return request({
    url: `/json/user/changePassword`,
    method: 'post',
    data
  })
}
