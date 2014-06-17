package cc.pp.mahout.recommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
	* <p>
	*  This code implements a boolean collaborative filtering algorithm. 
	* </p>
*/

class UserBaseRecommender {

	private UserBaseRecommender() {
	}

	public static final int NUM_OF_RECOMMENDATIONS_RETURNED = 10;
	public static boolean USE_LOG_LIKELIHOOD = true;
	public static boolean WRITE_TO_FILE = false;
	public static boolean KILL_EARLY = false;

	public static void main(String[] args) throws Exception {

		BufferedWriter out = new BufferedWriter(new FileWriter("recommendations.txt"));

		String INPUT_FILE = "ua.base.boolean-large.csv";
		if (args.length > 0) {
			INPUT_FILE = args[0];
		}

		UserSimilarity similarity;
		DataModel model = new FileDataModel(new File(INPUT_FILE));

		if (USE_LOG_LIKELIHOOD) {
			similarity = new LogLikelihoodSimilarity(model);
		} else {
			similarity = new TanimotoCoefficientSimilarity(model);
		}
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

		int counter = 0;
		LongPrimitiveIterator users = model.getUserIDs();
		while (users.hasNext()) {
			long userID = users.nextLong();
			List<RecommendedItem> recommendations = recommender.recommend(userID, NUM_OF_RECOMMENDATIONS_RETURNED);
			for (RecommendedItem recommendation : recommendations) {

				if (WRITE_TO_FILE) {
					out.write(String.format("%d,%d,%2.2f\n", userID, recommendation.getItemID(),
							recommendation.getValue()));
				} else {
					System.out.format("%d,%d,%2.2f\n", userID, recommendation.getItemID(), recommendation.getValue());
				}

				if (counter == 100 && KILL_EARLY) {
					out.close();
					System.exit(0);
				}
				counter++;
			}
		}

		out.close();
	}
}
