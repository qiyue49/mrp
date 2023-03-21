<template>
  <div class="all">
    <add-btn/>
    <import-btn/>
    <export-btn/>
  </div>
</template>

<script>
import AddBtn from './components/AddBtn.vue'
import ImportBtn from './components/ImportBtn.vue'
import ExportBtn from './components/ExportBtn.vue'
export default {
  components: { AddBtn, ImportBtn, ExportBtn }

}
</script>
<style lang="scss" scoped>
.all{
  display: flex;

  > {
    margin-right: 20px;
  }
}
</style>