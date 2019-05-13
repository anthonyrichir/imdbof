package com.imdbof.graphql;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.imdbof.domain.Movie;
import io.reactivex.BackpressureStrategy;
import io.reactivex.subjects.PublishSubject;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class Subscription implements GraphQLSubscriptionResolver, ApplicationListener<VoteAddedEvent> {

    private static final Logger log = LoggerFactory.getLogger(Subscription.class);

    private final PublishSubject<Movie> movieSubject;

    public Subscription() {
        movieSubject = PublishSubject.create();
    }

    public Publisher<Movie> voteAdded() {
        return movieSubject.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void onApplicationEvent(VoteAddedEvent event) {
        log.info("Subscription.VoteAddedEvent: {}", event.getMovie());
        movieSubject.onNext(event.getMovie());
    }
}
