<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="router-fade" mode="out-in">
        <keep-alive :include="cachedViews">
          <component :is="Component" :key="route.fullPath"/>
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script>
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
  }
}
</script>
<style lang="scss" scoped>
.app-main{
  margin-top: 30px;
}
</style>

