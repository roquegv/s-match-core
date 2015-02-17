package it.unitn.disi.smatch.preprocessors;

import it.unitn.disi.smatch.async.AsyncTask;
import it.unitn.disi.smatch.data.trees.IContext;
import it.unitn.disi.smatch.data.trees.INode;
import it.unitn.disi.smatch.oracles.ILinguisticOracle;

/**
 * Base class for context preprocessors.
 *
 * @author <a rel="author" href="http://autayeu.com/">Aliaksandr Autayeu</a>
 */
public abstract class BaseContextPreprocessor extends AsyncTask<Void, INode> implements IContextPreprocessor {

    // for task parameters
    protected final IContext context;
    protected final ILinguisticOracle linguisticOracle;

    public BaseContextPreprocessor(ILinguisticOracle linguisticOracle) {
        this.context = null;
        this.linguisticOracle = linguisticOracle;
    }

    public BaseContextPreprocessor(IContext context, ILinguisticOracle linguisticOracle) {
        this.context = context;
        this.linguisticOracle = linguisticOracle;
    }

    @Override
    protected Void doInBackground() throws Exception {
        final String threadName = Thread.currentThread().getName();
        try {
            Thread.currentThread().setName(Thread.currentThread().getName()
                    + " [" + this.getClass().getSimpleName() + ": context.size=" + context.nodesCount() + "]");

            String language = linguisticOracle.detectLanguage(context);
            linguisticOracle.readMultiwords(language);
            context.setLanguage(language);

            preprocess(context);
            return null;
        } finally {
            Thread.currentThread().setName(threadName);
        }
    }
}