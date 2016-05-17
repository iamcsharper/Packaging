package ru.xupoh.test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

public abstract class Service { 
	 
    /**
     * The delay of this service in milliseconds. This is immutable because 
     * dynamic delay changes are not supported when using executors. 
     */ 
    private final long delay; 
 
    /**
     * The result-bearing action that is used to cancel this service. 
     */ 
    private Future<?> future; 
 
    /**
     * Creates a new {@link Service}. 
     * 
     * @param delay 
     *            the delay of this service in milliseconds. 
     */ 
    public Service(long delay, TimeUnit unit) { 
        Preconditions.checkArgument(delay >= 0, "The delay of services must be >= 0."); 
        this.delay = unit.toMillis(delay); 
    } 
 
    /**
     * Creates a new {@link Service}. 
     * 
     * @param delay 
     *            the delay of this service in milliseconds. 
     */ 
    public Service(long delay) { 
        this(delay, TimeUnit.MILLISECONDS); 
    } 
 
    /**
     * Creates a new {@link Service} with no delay. 
     */ 
    public Service() { 
        this(0); 
    } 
 
    /**
     * The logic within this service that will be executed asynchronously. 
     * 
     * @param context 
     *            the context that this service is executed in. 
     */ 
    public abstract void execute(ServiceQueue context); 
 
    /**
     * Cancels this service and unregisters it from its service queue context. 
     */ 
    public final void cancel() { 
        if (future == null) 
            throw new IllegalStateException("Cannot cancel a Service that has not been submitted yet."); 
        if (!isDone()) 
            future.cancel(false); 
    } 
 
    /**
     * Blocks the calling thread until this service completes. It is preferred 
     * that {@code awaitSilently()} be used instead of this as there is little 
     * to no reason why one would want to be notified of following exceptions. 
     * 
     * @throws InterruptedException 
     *             if the calling thread is interrupted while blocking. 
     * @throws ExecutionException 
     *             if the computation throws an exception. 
     * @throws CancellationException 
     *             if the computation was cancelled. 
     */ 
    public final void await() throws InterruptedException, ExecutionException, CancellationException { 
        if (future == null) 
            throw new IllegalStateException("Cannot await completion for a service that has not been submitted yet."); 
        future.get(); 
    } 
 
    /**
     * Silently blocks the calling thread until this service completes, 
     * discarding any thrown {@link InterruptedException}s, 
     * {@link CancellationException}s, and {@link ExecutionException}s. This 
     * should be preferred in use to {@code await()}. 
     */ 
    public final void awaitSilently() { 
        try { 
            await(); 
        } catch (InterruptedException | CancellationException | ExecutionException ignored) { 
 
        } 
    } 
 
    /**
     * Determines if this service has been cancelled or completed. 
     * 
     * @return {@code true} if this service has is finished, {@code false} 
     *         otherwise. 
     */ 
    public final boolean isDone() { 
        if (future == null) 
            return false; 
        return future.isDone(); 
    } 
 
    /**
     * Sets the value for {@link Service#future}. 
     * 
     * @param future 
     *            the new value to set. 
     */ 
    final void setFuture(Future<?> future) { 
        this.future = future; 
    } 
 
    /**
     * Gets the delay of this service in milliseconds. 
     * 
     * @return the delay of this service. 
     */ 
    public final long getDelay() { 
        return delay; 
    } 
}