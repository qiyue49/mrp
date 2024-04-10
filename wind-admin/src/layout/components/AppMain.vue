<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="router-fade" mode="out-in">
        <keep-alive :include="cachedViews">
          <el-card :shadow="route.meta.card === '0' ? 'never': 'always'" :class="route.meta.card === '0' ? 'root-card': ''">
            <component :is="Component" :key="route.fullPath"/>
          </el-card>
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
.root-card{
  --el-card-padding: 0;
  background-color: transparent;
  border: 0;
}
</style>

