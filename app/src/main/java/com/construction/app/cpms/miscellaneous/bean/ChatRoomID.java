package com.construction.app.cpms.miscellaneous.bean;


public final class ChatRoomID {
        // expected = UID1-UID2
        //received = UID2-UID1

        //expected param had the expected uid combination, received has received comb
        public static boolean isCombinationHavingIndividualIDs(String receivedUIDCombination, String loggedInUID, String receiverUID) {
            //example data
            receivedUIDCombination = receivedUIDCombination.trim();				// Jw405DV177dkOg2nBWAjsAERs8j1-4kE5XKKcm6VOBb7yISPfqKmW6Li2
            loggedInUID = loggedInUID.trim();		// Jw405DV177dkOg2nBWAjsAERs8j1
            receiverUID = receiverUID.trim();		//4kE5XKKcm6VOBb7yISPfqKmW6Li2

            //means the received combination is a match for the given loggedInUID and reCeivedUID
            if(receivedUIDCombination.contains(loggedInUID) && receivedUIDCombination.contains(receiverUID)) {
                return true;
            }

            return false;

            //could have written as return receivedUIDCombination.contains(loggedInUID) && receivedUIDCombination.contains(receiverUID)
            //but for clearness when reading the code, left it as above.
        }


}
