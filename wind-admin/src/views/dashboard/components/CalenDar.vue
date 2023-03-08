<template>
  <div class="all">
    <VueSlickCarousel v-bind="settings">

      <div v-for="(item,index) in 10" :key="index" class="content">
        <span>{{ item }}</span>
      </div>
      <!-- <template #prevArrow="arrowOption">
        <div class="custom-arrow">
          {{ arrowOption.currentSlide }}/{{ arrowOption.slideCount }}
        </div>
      </template> -->
      <!-- <template #nextArrow="arrowOption">
        <div class="custom-arrow">
          {{ arrowOption.currentSlide }}/{{ arrowOption.slideCount }}
        </div>
      </template> -->
    </VueSlickCarousel>
  </div>
</template>

<script>
import VueSlickCarousel from 'vue-slick-carousel'
import 'vue-slick-carousel/dist/vue-slick-carousel.css'
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css'
export default {
  components: { VueSlickCarousel },
  data() {
    return {
      settings: {
        dots: false,
        arrows: true,
        infinite: true,
        initialSlide: 1,
        speed: 500,
        slidesToShow: 3,
        slidesToScroll: 1,
        swipeToSlide: true
        // centerPadding:'100px',
        // centerMode:true
        // focusOnSelect: true,
        // infinite: true,
        // slidesToShow: 3,
        // slidesToScroll: 1,
        // speed: 500
        // ,variableWidth:true
      }
    }
  }
}
</script>

<style>

</style>