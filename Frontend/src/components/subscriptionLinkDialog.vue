<template>
  <div
      class="absolute min-h-full min-w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-20 md:pt-40"
      v-if="showDialog" @click="backdropClicked" ref="backdrop">
    <div class="fixed top-4 bg-error rounded-xl px-2 py-2 text-white flex" v-if="linkNotCopiedAlert">
      <i class="pi pi-times-circle text-xl"/>
      <div>{{ local.copyUnsuccessful }}</div>
    </div>
    <div class="fixed top-4 bg-success rounded-xl px-2 py-2 text-white flex" v-if="linkCopiedAlert">
      <i class="pi pi-check-circle text-xl"/>
      <div>{{ local.copySuccessful }}</div>
    </div>
    <div
        class="bg-background-3 text-info-3 px-6 w-full md:w-fit md:max-w-[350px] rounded-xl flex flex-col py-4 bottom-0 max-h-[550px] md:bottom-auto md:max-h-[600px] fixed md:sticky md:top-10">
      <div
          @click="emits('closeDialog')"
          v-if="displayHelper(windowWidth).mdAndUp"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <i class="pi pi-times text-base mx-1"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.subscriptionUrl }}</div>
      <div class="flex flex-col overflow-y-scroll no-scrollbar">
        <div class="bg-white py-8 rounded-xl cursor-pointer flex justify-center" @click="copyUrl">
          <qrcode-vue :value="link" :size="200" :options="qrOptions"></qrcode-vue>
        </div>
        <div class="border border-primary-3 rounded-xl text-info-3 break-words px-6 py-4 mt-4 relative cursor-pointer"
             style="direction: ltr" @click="copyUrl">{{ link }}
        </div>
        <input type="hidden" id="copyUrl" :value="link">
      </div>
      <div class="text-success border border-success rounded-md mt-4 py-1 text-center">{{ local.clickToCopy }}</div>

    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import QrcodeVue from "qrcode.vue";
import {displayHelper} from "../helpers/displayHelper.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

const props = defineProps(['link', 'showDialog'])
const emits = defineEmits(['closeDialog'])

const qrOptions = reactive({
  typeNumber: 10, // Adjust typeNumber as needed
  errorCorrectionLevel: "H", // Adjust errorCorrectionLevel as needed
})

let linkCopiedAlert = ref(false)
let linkNotCopiedAlert = ref(false)


const copyUrl = () => {
  let urlToCopy = document.querySelector("#copyUrl");
  urlToCopy.setAttribute("type", "text"); // 不是 hidden 才能複製
  urlToCopy.select();

  try {
    let successful = document.execCommand("copy");
    linkCopiedAlert.value = true
    setTimeout(() => {
      linkCopiedAlert.value = false
    }, 1000)
  } catch (err) {
    linkNotCopiedAlert.value = true
    setTimeout(() => {
      linkNotCopiedAlert.value = false
    }, 1000)
  }
  /* unselect the range */
  urlToCopy.setAttribute("type", "hidden");
  window.getSelection().removeAllRanges();
}

let backdrop = ref(null)
const backdropClicked = (data) => {
  if (data.target === backdrop.value) {
    emits('closeDialog')
  }
}
</script>