import service from '@/utils/request'

export function login(username, password) {
  const data = {
    username,
    password
  }
  return service({
    url: '/sso/oauth2/accessToken',
    method: 'post',
    data
  })
}

export function logout(token) {
  return service({
    url: '/sso/oauth2/revokeToken',
    method: 'post',
    data: { accessToken: token }
  })
}

export function refreshToken(refreshToken) {
  return service({
    url: '/sso/oauth2/refreshToken',
    method: 'post',
    data: { refreshToken }
  })
}
