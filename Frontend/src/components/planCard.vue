<template>
  <div class="rounded-xl bg-background-3 p-6 text-info-3 mb-4"
       :class="{'mr-4' : !isRtl && ((displayHelper(windowWidth).sm && index%2 === 0) || (displayHelper(windowWidth).md && (index%3 === 0 || index%3 === 1)) || (displayHelper(windowWidth).lgAndUp && (index%4 === 0 || index%4 === 1 || index%4 === 2))) , 'ml-4' : isRtl && ((displayHelper(windowWidth).sm && index%2 === 0) || (displayHelper(windowWidth).md && (index%3 === 0 || index%3 === 1)) || (displayHelper(windowWidth).lgAndUp && (index%4 === 0 || index%4 === 1 || index%4 === 2)))}">
    <div class="flex justify-end">
      <div class="cursor-pointer mx-2" @click="emits('editPlan' , props.plan)">
        <i class="pi pi-pencil text-lg md:text-xl text-warning"/>
      </div>
      <div class="cursor-pointer" @click="emits('deletePlan' , props.plan)">
        <i class="pi pi-trash text-lg md:text-xl text-error"/>
      </div>
    </div>
    <div>
      {{ local.totalFlow }}&nbsp;:&nbsp{{ plan.totalFlow }}&nbspGB
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.periodLength }}&nbsp;:&nbsp{{ plan.periodLength }}
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.price }}&nbsp;:&nbsp{{ plan.price }}
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {displayHelper} from "../helpers/displayHelper.js";

const local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

const props = defineProps(['plan', 'index'])
const emits = defineEmits(['editPlan', 'deletePlan'])

const isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})


</script>