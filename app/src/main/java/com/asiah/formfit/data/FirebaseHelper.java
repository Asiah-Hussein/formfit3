// File: app/src/main/java/com/asiah/formfit/data/FirebaseHelper.java
// Add these methods to your existing FirebaseHelper class

/**
 * Load user exercises from Firebase
 */
public void loadUserExercises(final FirebaseDataListener<List<Exercise>> listener) {
    // Get current Firebase user
    String firebaseUserId = getCurrentUserId();
    if (firebaseUserId == null) {
        if (listener != null) {
            listener.onDataFailed("User not authenticated");
        }
        return;
    }

    // Get exercises from Firebase
    exercisesRef.orderByChild("userId").equalTo(firebaseUserId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Exercise> exercises = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            // Parse exercise data
                            String name = snapshot.child("name").getValue(String.class);
                            int duration = snapshot.child("duration").getValue(Integer.class);
                            float formAccuracy = snapshot.child("formAccuracy").getValue(Float.class);
                            int reps = snapshot.child("reps").getValue(Integer.class);
                            int calories = snapshot.child("calories").getValue(Integer.class);
                            Long timestamp = snapshot.child("timestamp").getValue(Long.class);

                            // Create exercise
                            Exercise exercise = new Exercise();
                            exercise.setName(name);
                            exercise.setDuration(duration);
                            exercise.setFormAccuracy(formAccuracy);
                            exercise.setReps(reps);
                            exercise.setCalories(calories);
                            exercise.setTimestamp(new Date(timestamp));
                            exercise.setSynced(true);

                            exercises.add(exercise);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing exercise data: " + e.getMessage());
                        }
                    }

                    if (listener != null) {
                        listener.onDataLoaded(exercises);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (listener != null) {
                        listener.onDataFailed(databaseError.getMessage());
                    }
                }
            });
}

/**
 * Sync all unsynced exercises to Firebase
 */
public void syncUnsyncedExercises(final SyncListener listener) {
    // Get unsynced exercises from local database
    List<Exercise> unsyncedExercises = dbHelper.getUnsyncedExercises();

    if (unsyncedExercises.isEmpty()) {
        // No unsynced exercises
        if (listener != null) {
            listener.onSyncComplete();
        }
        return;
    }

    // Sync each exercise
    final int[] syncCount = {0};
    for (final Exercise exercise : unsyncedExercises) {
        syncExercise(exercise, new SyncListener() {
            @Override
            public void onSyncComplete() {
                syncCount[0]++;
                if (syncCount[0] == unsyncedExercises.size()) {
                    // All exercises synced
                    if (listener != null) {
                        listener.onSyncComplete();
                    }
                }
            }

            @Override
            public void onSyncFailed(String error) {
                // Continue syncing other exercises
                syncCount[0]++;
                if (syncCount[0] == unsyncedExercises.size()) {
                    // All exercises processed
                    if (listener != null) {
                        listener.onSyncFailed("Some exercises failed to sync");
                    }
                }
            }
        });
    }
}