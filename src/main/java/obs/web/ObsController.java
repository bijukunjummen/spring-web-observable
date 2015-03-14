package obs.web;


import obs.model.Message;
import obs.service.Service1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import java.util.concurrent.CompletableFuture;

@RestController
public class ObsController {

    @Autowired
    private Service1 service1;

    @RequestMapping("/getAMessageObsBlocking")
    public Message getAMessageObsBlocking() {
        return service1.getAMessageObs().toBlocking().first();
    }


    @RequestMapping("/getAMessageObsAsync")
    public DeferredResult<Message> getAMessageAsync() {
        Observable<Message> o = this.service1.getAMessageObs();
        DeferredResult<Message> deffered = new DeferredResult<>(90000);
        o.subscribe(m -> deffered.setResult(m), e -> deffered.setErrorResult(e));
        return deffered;
    }

    @RequestMapping("/getAMessageFutureBlocking")
    public Message getAMessageFutureBlocking() throws Exception {
        return service1.getAMessageFuture().get();
    }

    @RequestMapping("/getAMessageFutureAsync")
    public DeferredResult<Message> getAMessageFutureAsync() {
        DeferredResult<Message> deffered = new DeferredResult<>(90000);
        CompletableFuture<Message> f = this.service1.getAMessageFuture();
        f.whenComplete((res, ex) -> {
            if (ex != null) {
                deffered.setErrorResult(ex);
            } else {
                deffered.setResult(res);
            }
        });
        return deffered;
    }


    @RequestMapping("/quickMessage")
    public Message getAFastMessage() {
        return new Message("quick message");
    }


}
