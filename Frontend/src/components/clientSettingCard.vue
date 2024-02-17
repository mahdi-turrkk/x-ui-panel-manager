<template>
  <div class="rounded-xl bg-background-3 p-6 text-info-3 mb-4" :class="{'md:mr-4' : !isRtl , 'md:ml-4' : isRtl}">
    <div class="flex justify-between">
      <div>
        {{ setting.os.toUpperCase() }}
      </div>
      <div class="cursor-pointer mx-2" @click="isEditing = true">
        <i class="pi pi-pencil text-lg md:text-xl text-warning"/>
      </div>
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.clients }}&nbsp;:&nbsp&nbsp{{ !isEditing ? setting.clients : ''}}
    </div>
    <div class="mt-2 md:mt-4" v-if="isEditing">
      <input type="text" :placeholder="local.clients" v-model="setting.clients"
             class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
             :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}"
      />
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.fragment }}&nbsp;: &nbsp&nbsp{{ !isEditing ? setting.applyFragment ? local.active : local.inactive : ''}}
    </div>
    <div class="mt-2 md:mt-4 flex" v-if="isEditing">
      <div class="flex" :class="{'mr-4' : !isRtl , 'ml-4' : isRtl}">
        <input :class="{'mr-2' : !isRtl , 'ml-2' : isRtl}" type="checkbox" v-model="setting.applyFragment">
        {{ local.active }}
      </div>
      <div class="flex">
        <input :class="{'mr-2' : !isRtl , 'ml-2' : isRtl}" type="checkbox" :checked="!setting.applyFragment" @click="($event) => setting.applyFragment = !setting.applyFragment">
        {{ local.inactive }}
      </div>
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.fragmentLength }} : &nbsp&nbsp{{ !isEditing ? setting.fragmentLength : ''}}
    </div>
    <div class="mt-2 md:mt-4" v-if="isEditing">
      <input type="text" :placeholder="local.fragmentLength" v-model="setting.fragmentLength"
             class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
             :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}"
      />
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.fragmentInterval }}&nbsp;: &nbsp&nbsp{{ !isEditing ? setting.fragmentInterval : ''}}
    </div>
    <div class="mt-2 md:mt-4" v-if="isEditing">
      <input type="text" :placeholder="local.fragmentInterval" v-model="setting.fragmentInterval"
             class="w-full shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
             :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}"
      />
    </div>
    <div class="mt-2 md:mt-4">
      {{ local.sendToClient }}&nbsp;: &nbsp&nbsp{{ !isEditing ? setting.generateJson? 'JSON' : setting.generateV2rayLink ? 'Link' : '' : ''}}
    </div>
    <div class="mt-2 md:mt-4 flex" v-if="isEditing">
      <div class="flex" :class="{'mr-4' : !isRtl , 'ml-4' : isRtl}">
        <input :class="{'mr-2' : !isRtl , 'ml-2' : isRtl}" type="checkbox" v-model="setting.generateJson" @click="setting.generateJson = !setting.generateJson;setting.generateV2rayLink = !setting.generateJson">
        JSON
      </div>
      <div class="flex">
        <input :class="{'mr-2' : !isRtl , 'ml-2' : isRtl}" type="checkbox" :checked="!setting.generateJson" @click="setting.generateV2rayLink = !setting.generateV2rayLink;setting.generateJson = !setting.generateV2rayLink">
        Link
      </div>
    </div>
    <div class="mt-2 md:mt-4 flex justify-end" v-if="isEditing">
      <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
              @click="save">
        {{ local.save }}
      </button>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {displayHelper} from "../helpers/displayHelper.js";
import axios from "axios";
import {useDataStore} from "../store/dataStore.js";

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

const props = defineProps(['setting'])
const emits = defineEmits(['errorOccurred'])

let isEditing = ref(false)

const isRtl = computed(() => {
  return useLocalization().getDirection === 'rtl'
})

const save = () => {
  console.log(props.setting)
  axios.put(`${useDataStore().getServerAddress}/os-setting/update?id=${props.setting.id}`,
      props.setting,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      })
      .then((response) => {
        isEditing.value = false
      }).catch((err) => {
        emits('errorOccurred')
  })
}


</script>