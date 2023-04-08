import request from '@/utils/request'

export function login(username, password) {
  const data = {
    grant_type: 'password',
    client_id: 'cloud-upms',
    client_secret: 'cloud-upms',
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
    data: { access_token: token }
  })
}

export function refreshToken(refreshToken) {
  const data = {
    refreshToken
  }
  return request({
    url: '/sso/oauth2/refreshToken',
    method: 'post',
    data
  })
}
