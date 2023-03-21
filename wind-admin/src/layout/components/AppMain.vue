<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="router-fade" mode="out-in">
        <keep-alive :include="cachedViews">
          <el-card :class="{'elcard': true}">
            <component :is="Component" :key="route.fullPath" class="ccc" />
          </el-card>
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script>
import { useRoute } from 'vue-router'
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      return this.$store.tagsViewStore.cachedViews
    },
    reload() {
      return this.$store.tagsViewStore.isReload
    },
    key() {
      return this.$route.path
    }
  },
  mounted() {
    const route = useRoute()
    console.log('dsasa', route)
  }
}
</script>
<style lang="scss" scoped>
:deep(.elcard){
    background-color: #F3F5F8;
  }
</style>

