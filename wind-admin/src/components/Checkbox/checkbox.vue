<template>
  <el-checkbox-group v-model="checkList" @change="change">
    <el-checkbox
      v-for="item in list"
      :key="'type' + item.value"
      :label="item[props.value]">
      {{ item[props.label] }}
    </el-checkbox>
  </el-checkbox-group>
</template>

<script>
export default {
  name: 'Checkbox',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    list: {
      type: Array,
      default() {
        return []
      }
    },
    props: {
      type: Object,
      default() {
        return { value: 'value', label: 'label' }
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      checkList: []
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.checkList = []
        if (val === undefined || val === '') {
          return
        }
        this.checkList = val.split(',').filter(item => {
          return item !== ''
        })
      }
    }
  },
  methods: {
    change(val) {
      this.$emit('update:modelValue', val.join(','))
    }

  }

}
</script>

<style scoped>

</style>
