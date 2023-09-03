<template>
  <div class="absolute h-full w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-40"
       v-if="showDialog">
    <div class="bg-background-3 text-info-3 px-6 w-80 rounded-xl flex flex-col py-6 relative">
      <div class="rounded-md bg-success flex items-center text-white justify-center py-1 absolute left-6 right-6" v-if="linkCopiedAlert">
        <check-circle-icon class="h-4 w-4 mx-2"/>
        {{ local.copySuccessful }}
      </div>
      <div class="rounded-md bg-error flex items-center text-white justify-center py-1 absolute left-6 right-6" v-if="linkNotCopiedAlert">
        <X-circle-icon class="h-5 w-5 mx-2"/>
        {{ local.copyUnsuccessful }}
      </div>
      <div
          @click="emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subscriptionUrl }}</div>
      <div class="bg-white p-8 rounded-xl cursor-pointer" @click="copyUrl">
        <qrcode-vue :value="link" :size="200" :options="qrOptions"></qrcode-vue>
      </div>
      <div class="border border-primary-3 rounded-xl text-info-3 break-words px-6 py-4 mt-4 relative cursor-pointer" style="direction: ltr" @click="copyUrl">{{ link }}</div>
      <input type="hidden" id="copyUrl" :value="link">
      <div class="text-success border border-success rounded-md mt-4 py-1 text-center">{{ local.clickToCopy }}</div>
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon , CheckCircleIcon, XCircleIcon} from "@heroicons/vue/24/outline";
import QrcodeVue from 'qrcode.vue'

let local = computed(() => {
  return useLocalization().getLocal
})

const props = defineProps(['link' , 'showDialog'])
const emits = defineEmits(['closeDialog'])

const qrOptions = reactive({
  typeNumber: 10, // Adjust typeNumber as needed
  errorCorrectionLevel: "H", // Adjust errorCorrectionLevel as needed
})

let linkCopiedAlert = ref(false)
let linkNotCopiedAlert = ref(false)


const copyUrl = ()=> {
  let urlToCopy = document.querySelector("#copyUrl");
  urlToCopy.setAttribute("type", "text"); // 不是 hidden 才能複製
  urlToCopy.select();

  try {
    let successful = document.execCommand("copy");
    linkCopiedAlert.value = true
    setTimeout(()=>{linkCopiedAlert.value = false} , 1000)
  } catch (err) {
    linkNotCopiedAlert.value = true
    setTimeout(()=>{linkNotCopiedAlert.value = false} , 1000)
  }
  /* unselect the range */
  urlToCopy.setAttribute("type", "hidden");
  window.getSelection().removeAllRanges();
}
</script>