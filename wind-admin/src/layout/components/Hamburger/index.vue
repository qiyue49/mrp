<template>
  <div @click="toggleClick">
    <span><img src="@/assets/img/dashboard/navigation.svg" alt="" class="hamburger"/></span>
  </div>
</template>

<script>
export default {
  name: 'Hamburger',
  props: {
    isActive: {
      type: Boolean,
      default: false
    }
  },
  emits: ['toggleClick'],
  methods: {
    toggleClick() {
      this.$emit('toggleClick')
    }
  }
}
</script>

<style scoped>
.hamburger {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
  margin-left: 30px;
}

.hamburger.is-active {
  transform: rotate(180deg);
}
</style>
