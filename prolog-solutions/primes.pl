checker(X, Y, Z):-
	Z < Y,
	\+(0 is mod(X, Z)),
	Z1 is Z + 1,
	checker(X, Y, Z1).

checker(X, Y, Z):-
	0 is mod (X, Z), !.

prime(1) :- !.
prime(2) :- !.
prime(3) :- !.
prime(N) :-
		N > 3,
		N1 is sqrt(N),
		\+ checker(N, N1, 2).

composite(N) :-
	\+ prime(N).

finder(N, I) :-
		N > 1,
    prime(I),
    0 is mod(N, I), !.

prime_divisors(N, H) :- prime_divisor(N, H, 2), !.


prime_divisor(1, [], R) :- !.

prime_divisor(N, [H | T], R) :-
  finder(N, R),
  H is R,
  N1 is N // R,
  prime_divisor(N1, T, R), !.

prime_divisor(N, T, R) :-
 	\+ finder(N, R),
  P is R + 1,
  prime_divisor(N, T, P), !.


concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).


del([H1, H2|T], R) :-
	\+ H1 = H2,
	concat([H2], T, P),
	del(P, S),
	concat([H1], S, R),!.

del([H1, H2 | T], R) :-
	H1 = H2,
	del([H1| T], R), !.

del([], []) :- !.

del([H],[H]) :- !.


unique_prime_divisors(N, H) :-
	prime_divisors(N,V),
	del(V,H).






