import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

const useSession = false

const expireDate = 1 // 1天

export function getToken() {
  return useSession ? sessionStorage.getItem(TokenKey) : Cookies.get(TokenKey)
}

export function setToken(token) {
  return useSession ? sessionStorage.setItem(TokenKey, token) : Cookies.set(TokenKey, token, { expires: expireDate })
}

export function removeToken() {
  return useSession ? sessionStorage.removeItem(TokenKey) : Cookies.remove(TokenKey)
}

const RefreshTokenKey = 'Admin-REFRESH_TOKEN'

export function getRefreshToken() {
  return useSession ? sessionStorage.getItem(RefreshTokenKey) : Cookies.get(RefreshTokenKey)
}

export function setRefreshToken(token) {
  return useSession ? sessionStorage.setItem(RefreshTokenKey, token) : Cookies.set(RefreshTokenKey, token, { expires: expireDate })
}

export function removeRefreshToken() {
  return useSession ? sessionStorage.removeItem(RefreshTokenKey) : Cookies.remove(RefreshTokenKey)
}
