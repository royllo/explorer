<template>
  <!-- ============================================================================================================= -->
  <!-- Request view (displayed on request page).                                                                  -->
  <!-- ============================================================================================================= -->

  <!-- If request id is null -->
  <div class="flex justify-center" v-if="isNaN(requestId)">Request id must be a number</div>

  <!-- Request description -->
  <div class="flex justify-center px-2" v-if="data != null">
    <div class="block p-6 rounded-lg shadow-lg bg-white w-2/3">
      <h5 class="text-gray-900 text-xl leading-tight font-medium mb-2">Request n°{{ data?.request?.id }}</h5>
      <hr>
      <br>
      <p class="text-gray-700 text-base mb-4 truncate md:text-clip">
        <!-- ======================================================================================================= -->
        <b>Creator</b><br>
        Username: {{ data?.request?.creator?.username }}<br>
        <br>
        <!-- ======================================================================================================= -->
        <b>Request</b><br>
        Genesis point: {{ data?.request?.genesisPoint }}<br>
        Asset name : {{ data?.request?.name }}<br>
        Asset metadata: {{ data?.request?.metaData }}<br>
        Asset id: {{ data?.request?.outputIndex }}<br>
        Proof:<br>
        <textarea id="message" rows="4" disabled
                  class="block w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
          {{ data?.request?.proof }}
          </textarea>
        <br>
        <!-- ======================================================================================================= -->
        <b>Status</b><br>
        {{ data?.request?.status === "OPENED" ? "Opened" : "" }}
        {{ data?.request?.status === "SUCCESS" ? "Success" : "" }}
        {{ data?.request?.status === "FAILURE" ? "Failure" : "" }}
        <br>{{ data?.request?.errorMessage }}
      </p>
    </div>
  </div>

</template>

<script lang="ts" setup>
import request from '~/queries/request.gql'

// =====================================================================================================================
// "requestId" is the value of the request id - Comes from URL parameter.
const requestId = computed(() => {
  return useRoute().params.requestId;
});

// =====================================================================================================================
// Executing the graphQL query that retrieves a request.
const variables = {id: requestId.value};
const {data} = await useAsyncQuery(request, variables);

</script>