<template>
  <div class="absolute h-full w-full bg-gray-900 bg-opacity-70 top-0 left-0  z-50 flex justify-center items-start pt-40"
       v-if="showDialog">
    <div class="bg-background-3 text-info-3 px-6 min-w-fit rounded-xl flex flex-col py-4 relative">
      <div
          @click="emits('closeDialog')"
          class="bg-background-3 text-error w-6 h-6 rounded-full absolute -top-2 -right-2 flex justify-center items-center border border-error hover:brightness-125 cursor-pointer">
        <x-mark-icon class="h-4 w-4"/>
      </div>
      <div class="text-center font-bold mb-4">{{ local.server }}</div>
      <input type="text" :placeholder="local.serverUrl" v-model="serverUrl"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <input type="text" :placeholder="local.username" v-model="serverUsername"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <input type="text" :placeholder="local.password" v-model="serverPassword"
             class="w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <div class="flex justify-end">
        <button
            @click="emits('closeDialog')"
            class="rounded-xl py-1 px-7 text-error border border-error brightness-75 hover:brightness-125 transition-all duration-150 mx-2">
          {{ local.cancel }}
        </button>
        <button class="rounded-xl bg-primary-3 py-1 px-6 text-white hover:brightness-125 transition-all duration-150"
                @click="saveServer">
          {{ local.save }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, ref, watch} from "vue";
import {useLocalization} from "../store/localizationStore.js";
import {XMarkIcon} from "@heroicons/vue/24/solid/index.js";

let local = computed(() => {
  return useLocalization().getLocal
})

let serverId = ref(undefined)
let serverUrl = ref('')
let serverUsername = ref('')
let serverPassword = ref('')
const props = defineProps(['showDialog', 'server'])

watch(() => props.server, (newVal) => {
  serverId.value = props.server.serverId
  serverUrl.value = props.server.serverUrl
  serverUsername.value = props.server.username
  serverPassword.value = props.server.password
})

const emits = defineEmits(['closeDialog'])

</script>