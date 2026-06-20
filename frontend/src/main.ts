import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { setup } from '@/router'
import App from './App.vue'
import { useAppStore } from './stores/app'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
setup(app)

const appStore = useAppStore()
appStore.initThreshold()

app.mount('#app')
