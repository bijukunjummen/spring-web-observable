package obs.service;

import obs.Util;
import obs.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Service1 {
    private static final Logger logger = LoggerFactory.getLogger(Service1.class);

    private final ExecutorService customObservableExecutor = Executors.newFixedThreadPool(10);

    private final ExecutorService futureExecutor = Executors.newFixedThreadPool(10);

    public Observable<Message> getAMessageObs() {
        return Observable.<Message>create(s -> {
            logger.info("Start: Executing slow task in Service 1");
            Util.delay(1000);
            s.onNext(new Message("data 1"));
            logger.info("End: Executing slow task in Service 1");
            s.onCompleted();
        }).subscribeOn(Schedulers.from(customObservableExecutor));
    }

    public CompletableFuture<Message> getAMessageFuture() {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Start: Executing slow task in Service 1");
            Util.delay(1000);
            logger.info("End: Executing slow task in Service 1");
            return new Message("data 1");
        }, futureExecutor);
    }


}
