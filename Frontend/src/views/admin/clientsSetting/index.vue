<template>
  <admin-layout>
    <div class="flex justify-center">
      <div class="fixed top-4 bg-error rounded-xl px-2 py-2 text-white flex" v-if="showErrorMessage">
        <i class="pi pi-times-circle text-xl"/>
        <div>{{ errorMessage }}</div>
      </div>
    </div>
    <div class="rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.clientsSetting }}</div>
    </div>
    <div class="flex justify-center mt-6" v-if="loading">
      <loader/>
    </div>
    <div class="rounded-xl w-full py-3 px-3 grid grid-cols-1 md:grid-cols-2" v-if="!loading && settings.length > 0">
      <client-setting-card v-for="(setting , index) in settings" :setting="setting" @error-occurred="errorOccurred"/>
    </div>
    <div class="w-full flex justify-center items-center pt-12" v-if="!loading && settings.length === 0">
      <div class="border-t-primary-3 w-full border-t-2 flex justify-center mx-2 md:mx-6">
        <div class="w-fit -mt-4 text-info-3 text-base md:text-xl bg-background-1 px-2">{{ local.noRecords }}</div>
      </div>
    </div>
  </admin-layout>
</template>

<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {computed, onMounted, reactive, ref, watch} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";
import Loader from "../../../components/loader.vue";
import ClientSettingCard from "../../../components/clientSettingCard.vue";

const local = computed(() => {
  return useLocalization().getLocal
})

let loading = ref(true)

let settings = ref([
  {
    id: 1,
    os: 'Windows',
    clients: 'V2rayN',
    applyFragment: false,
    fragmentInterval: '50-100',
    fragmentLength: '10-20',
    generateJson: true,
    generateV2rayLink: false,
    packets: '10-20',
    globalSettingId: 1
  }
])



const isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

const getSettings = () => {
  loading.value = true
  axios.get(`${useDataStore().getServerAddress}/global-setting/get`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      })
      .then((response) => {
        settings.value = response.data.osSettingDtos
        loading.value = false
      })
}

onMounted(() => {
  getSettings()
})

let errorMessage = ref('')
let showErrorMessage = ref(false)
const errorOccurred = () => {
  errorMessage.value = local.value.errorOccurredWhileSavingOsSetting
  showErrorMessage.value = true
  setTimeout(() => {
    showErrorMessage.value = false
  }, 2000)
}

</script>