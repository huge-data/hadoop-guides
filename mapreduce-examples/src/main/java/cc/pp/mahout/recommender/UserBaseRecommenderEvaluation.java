package cc.pp.mahout.recommender;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
	* <p>
	*  This code evaluates a boolean collaborative filtering algorithm. 
	* </p>
*/

class UserBaseRecommenderEvaluation {

	private UserBaseRecommenderEvaluation() {
	}

	public static void main(String[] args) throws Exception {

		DataModel model = new FileDataModel(new File("ua.base.boolean-large.csv"));

		RecommenderBuilder builder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new LogLikelihoodSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};

		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = evaluator.evaluate(builder, null, model, null, 1,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);

		// on average, about P % of recommendations are good
		System.out.println("PRECISION: On Avarege, about " + stats.getPrecision() * 100.0
				+ "% of recommendations are good");

		// %R of good recommenations are amont those recommended
		System.out.println("RECALL: " + stats.getRecall() * 100.0
				+ "% of good recommenations are among those recommended");

	}

}
