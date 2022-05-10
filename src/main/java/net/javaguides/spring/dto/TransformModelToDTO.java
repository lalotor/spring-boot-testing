package net.javaguides.spring.dto;

public interface TransformModelToDTO<T,S> {

  public S transformFrom1stTo2nd(T t);

  public T transformFrom2ndTo1st(S t);

}
