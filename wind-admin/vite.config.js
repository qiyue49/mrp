import { fileURLToPath, URL } from 'node:url'

import { loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import eslintPlugin from 'vite-plugin-eslint'

// https://vitejs.dev/config/
export default ({ mode }) => {
  // 获取 .env 环境配置文件
  const env = loadEnv(mode, process.cwd())
  return {
    // 静态资源服务的文件夹
    publicDir: 'public',
    base: './',
    server: {
      host: '0.0.0.0',
      port: 9527,
      open: true, // 运行自动打开浏览器
      proxy: {
        [env.VITE_APP_BASE_API]: {
          // 本地API地址
          target: 'http://localhost:8082',
          changeOrigin: true,
          rewrite: path => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), '')
        },
        [env.VITE_APP_BASE_WEBSOCKET]: {
          // 本地API地址
          target: 'ws://localhost:8082',
          changeOrigin: true,
          rewrite: path => path.replace(new RegExp('^' + env.VITE_APP_BASE_WEBSOCKET), '')
        }
      }
    },
    plugins: [
      vue(),
      eslintPlugin({
        include: ['src/**/*.js', 'src/**/*.vue', 'src/*.js', 'src/*.vue'], cache: false, emitWarning: true, emitError: true
      }),
      createSvgIconsPlugin({
        // 指定需要缓存的图标文件夹
        iconDirs: [resolve(process.cwd(), 'src/assets/icons/svg')],
        // 指定symbolId格式
        symbolId: 'icon-[dir]-[name]'
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
      extensions: ['.js', '.vue', '.json']
    },
    // 预览设置vite build打包之后，会生成dist文件
    // 然后运行vite preview，vite会创建一个服务器来运行打包之后的文件
    preview: {
      port: 9528, // 端口号
      host: 'localhost',
      open: true // 是否自动打开浏览器
    },
    // 打包配置 vite build
    build: {
      // 指定输出路径
      outDir: 'dist',
      // 生成静态资源的存放路径
      assetsDir: 'assets',
      // 小于此阈值的导入或引用资源将内联为 base64 编码，以避免额外的 http 请求。设置为 0 可以完全禁用此项
      assetsInlineLimit: 0,
      // 启用/禁用 CSS 代码拆分
      cssCodeSplit: true,
      // 构建后是否生成 source map 文件
      sourcemap: false,
      // 自定义底层的 Rollup 打包配置
      rollupOptions: {
        input: {
          // 可以配置多个，表示多入口
          index: resolve(__dirname, 'index.html')
          // project:resolve(__dirname,"project.html")
        },
        output: {
          // chunkFileNames:'static/js/[name]-[hash].js',
          // entryFileNames:"static/js/[name]-[hash].js",
          // assetFileNames:"static/[ext]/name-[hash].[ext]"
        }
      },
      // 默认情况下，若 outDir 在 root 目录下，则 Vite 会在构建时清空该目录。
      emptyOutDir: true,
      // chunk 大小警告的限制
      chunkSizeWarningLimit: 500
    }
  }
}
