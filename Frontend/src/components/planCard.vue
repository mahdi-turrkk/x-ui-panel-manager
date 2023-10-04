<template>
  <div class="rounded-xl bg-background-3 p-6 text-info-3 mb-4" :class="{'mr-4' : !isRtl , 'ml-4' : isRtl}">
    <div class="flex justify-end">
      <div class="cursor-pointer mx-2" @click="emits('editPlan' , props.plan)">
        <pencil-square-icon class="w-6 h-6 text-warning"/>
      </div>
      <div class="cursor-pointer" @click="emits('deletePlan' , props.plan)">
        <trash-icon class="w-6 h-6 text-error"/>
      </div>
    </div>
    <div>
      {{local.totalFlow}}&nbsp;:&nbsp{{plan.totalFlow}}&nbspGB
    </div>
    <div class="mt-2 md:mt-4">
      {{local.periodLength}}&nbsp;:&nbsp{{plan.periodLength}}
    </div>
    <div class="mt-2 md:mt-4">
      {{local.price}}&nbsp;:&nbsp{{plan.price}}
    </div>
  </div>
</template>

<script setup>
import {computed} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {PencilSquareIcon,TrashIcon} from "@heroicons/vue/24/outline/index.js";

const local = computed(()=>{return useLocalization().getLocal})

const props = defineProps(['plan'])
const emits = defineEmits(['editPlan' , 'deletePlan'])

const isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})


</script>