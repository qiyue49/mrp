import request from '@/utils/request'

export function login(username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/sso/oauth2/accessToken',
    method: 'post',
    data
  })
}

export function logout(token) {
  return request({
    url: '/sso/oauth2/revokeToken',
    method: 'post',
    data: { accessToken: token }
  })
}

export function refreshToken(refreshToken) {
  return request({
    url: '/sso/oauth2/refreshToken',
    method: 'post',
    data: { refreshToken }
  })
}
