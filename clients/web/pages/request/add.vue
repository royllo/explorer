<template>

  <div class="flex flex-wrap justify-center">

    <!-- ============================================================================================================= -->
    <!-- Add request form -->
    <div
        class="w-full max-w-xl p-4 bg-white border border-gray-200 rounded-lg shadow-md sm:p-6 md:p-8 dark:bg-gray-800 dark:border-gray-700 space-y-6">
      <h5 class="text-xl font-medium text-gray-900 dark:text-white">Register an asset</h5>

      <!-- Genesis point -->
      <div>
        <label for="genesisPoint" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Genesis
          point</label>
        <input type="text" id="genesisPoint" name="genesisPoint" v-model="genesisPoint"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder=""
               autofocus
               required>
      </div>

      <!-- Name -->
      <div>
        <label for="name" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Name</label>
        <input type="text" id="name" name="name" v-model="name"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder="" required>
      </div>

      <!-- Name -->
      <div>
        <label for="Meta data" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Meta data</label>
        <input type="text" id="metaData" name="metaData" v-model="metaData"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder="" required>
      </div>

      <!-- Asset id -->
      <div>
        <label for="Asset id" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Asset id</label>
        <input type="text" id="assetId" name="assetId" v-model="assetId"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder="" required>
      </div>

      <!-- Output index-->
      <div>
        <label for="Asset id" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Output
          index</label>
        <input type="text" id="outputIndex" name="outputIndex" v-model="outputIndex"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder="" required>
      </div>

      <!-- Proof-->
      <div>
        <label for="Proof" class="block mb-2 text-sm font-medium text-gray-900 dark:text-gray-300">Proof</label>
        <input type="text" id="proof" name="proof" v-model="proof"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"
               placeholder="" required>
      </div>

      <button @click="createRequest"
              class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        Add this asset
      </button>
    </div>

  </div>

</template>

<script lang="ts" setup>
import openedRequests from '~/queries/openedRequests.gql';
import addAssetRequest from '~/queries/addAssetRequest.gql';
import AddAssetRequestInputs from '~/queries/AddAssetRequestInputs.gql';

// =====================================================================================================================
// Form content.
const genesisPoint = ref('');
const name = ref('');
const metaData = ref('');
const assetId = ref('');
const outputIndex = ref('');
const proof = ref('');

// =====================================================================================================================
// Function used to save the request.
function createRequest() {

  // Building the mutation parameters with form data.
  const variables: { input: AddAssetRequestInputs } = {
    input: {
      genesisPoint: genesisPoint,
      name: name,
      metaData: metaData,
      assetId: assetId,
      outputIndex: outputIndex,
      proof: proof,
    }
  };

  // Calling the methods.
  const {mutate: addAssetRequestMutation, error} = useMutation(addAssetRequest, {variables: variables});
  addAssetRequestMutation();
  console.log("Error+" + error.value);
}

</script>