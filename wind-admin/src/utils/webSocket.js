import { store } from '@/stores'

let webSocket

function getURL() {
  const userId = store.userStore.userInfo.id
  console.log('import.meta.env.NODE_ENV', import.meta.env.NODE_ENV)
  console.log('import.meta.env.VITE_APP_BASE_WEBSOCKET', import.meta.env.VITE_APP_BASE_WEBSOCKET)
  console.log('import.meta.env.VITE_APP_ENV', import.meta.env.VITE_APP_ENV)

  if (import.meta.env.VITE_APP_ENV === 'proxy') {
    return `ws://${location.host}${import.meta.env.VITE_APP_BASE_WEBSOCKET}/json/web/server/${userId}`
  }
  if (import.meta.env.VITE_APP_ENV === 'tomcat') {
    return `${import.meta.env.VITE_APP_BASE_WEBSOCKET}/json/web/server/${userId}`
  }
  if (import.meta.env.VITE_APP_ENV === 'nginx') {
    return `ws://${location.host}${import.meta.env.VITE_APP_BASE_WEBSOCKET}/json/web/server/${userId}`
  }
}
export function webSocketConnect(onMessage) {
  console.log('getURL', getURL())
  webSocket = new WebSocket(getURL())
  initWebSocket(onOpen, onMessage)
}

export function webSocketConnectWithCallback(onOpenCallBack, onMessage) {
  webSocket = new WebSocket(getURL())
  initWebSocket(onOpenCallBack, onMessage)
}

export function initWebSocket(onOpen, onMessage) {
  webSocket.onerror = onError // 通讯异常
  webSocket.onopen = onOpen // 连接成功
  webSocket.onclose = onClose // 连接关闭时回调
  webSocket.onmessage = (event) => {
    onMessage(event)
  } // 收到消息时回调
}

export function onError() {
  /*
  * 通讯异常
  * */
  console.log('通讯异常')
}

export function onOpen() {
  /*
  * 连接成功
  * */
  console.log('通讯开始')
}

export function onClose() {
  /*
  * 关闭连接时回调函数
  * */
  console.log('通讯关闭')
}

export function webSocketSend(message) {
  console.log('webSokcetSend', message)
  webSocket.send(JSON.stringify(message))
}

export function webSocketClose() {
  webSocket.close()
}
