
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% MODEL
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

stored_prior_a_if_dmin(preflop,bet,0.0914920613147697).
stored_prior_a_if_dmin(flop,bet,0.219671944412803).
stored_prior_a_if_dmin(turn,bet,0.272498743086978).
stored_prior_a_if_dmin(river,bet,0.287785369105149).
stored_prior_a_if_dmin(preflop,fold,0.440211241041117).
stored_prior_a_if_dmin(flop,fold,0.235391274632646).
stored_prior_a_if_dmin(turn,fold,0.186324786324786).
stored_prior_a_if_dmin(river,fold,0.217130478253624).
stored_prior_a_if_dmin(preflop,call,0.372861012996811).
stored_prior_a_if_dmin(flop,call,0.152693928693473).
stored_prior_a_if_dmin(turn,call,0.167320261437908).
stored_prior_a_if_dmin(river,call,0.110314947508749).
stored_prior_a_if_dmin(preflop,check,0.0954356846473029).
stored_prior_a_if_dmin(flop,check,0.392242852261078).
stored_prior_a_if_dmin(turn,check,0.373856209150327).
stored_prior_a_if_dmin(river,check,0.384769205132478).

predict_distribution(A,B,[0.295178633368019,0.704821366631981]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0),player_action_current_game(A,B,C,raise),player_game_total_bets(A,B,D),D<3, !.
% 2031.0/2881.0=0.704963554321416
predict_distribution(A,B,[0.434311670160727,0.565688329839273]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0),player_preflop_raise_frequency(B,C),C>0.1,player_absolute_after(A,D),player_game_total_bets(A,D,E),E<2, !.
% 1618.0/2860.0=0.565734265734266
predict_distribution(A,B,[0.327140549273021,0.672859450726979]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0),player_preflop_raise_frequency(B,C),C>0.1, !.
% 1665.0/2474.0=0.672999191592563
predict_distribution(A,B,[0.469885057471264,0.530114942528736]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0),game_remaining_players(A,C),C<5,game_players_to_act_relative(A,D),D<2,player_absolute_after(A,E),player_aggression(E,F),F>1, !.
% 1152.0/2173.0=0.530142659917165
predict_distribution(A,B,[0.40632603406326,0.59367396593674]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0),game_remaining_players(A,C),C<5,game_players_to_act_relative(A,D),D<2, !.
% 1219.0/2053.0=0.593765221626887
predict_distribution(A,B,[0.532097399003874,0.467902600996126]) :- player_current_action(A,bet),game_total_bets_current_phase(A,0), !.
% 1922.0/3612.0=0.532115171650055
predict_distribution(A,B,[0.801319363601087,0.198680636398913]) :- player_current_action(A,bet),player_absolute_after(A,C),player_stack_ratio(A,C,D),D>0.5, !.
% 2064.0/2575.0=0.801553398058252
predict_distribution(A,B,[0.641,0.359]) :- player_current_action(A,bet), !.
% 1922.0/2998.0=0.641094062708472
predict_distribution(A,B,[0.663768115942029,0.336231884057971]) :- player_previous_action(A,B,C,raise),player_pot_odds(A,B,D),D<0.2, !.
% 1831.0/2758.0=0.663886874546773
predict_distribution(A,B,[0.0961038961038961,0.903896103896104]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),game_current_phase(A,preflop),game_players_to_act_relative(A,0), !.
% 2783.0/3078.0=0.904158544509422
predict_distribution(A,B,[0.319058947924927,0.680941052075073]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5,player_previous_action(A,B,D,call_small),game_total_bets_current_phase(D,E),E<2,player_action_current_game(A,B,F,call_small),game_total_bets(F,1), !.
% 2575.0/3781.0=0.681036762761174
predict_distribution(A,B,[0.351950603424081,0.648049396575919]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5,player_previous_action(A,B,D,call_small),game_total_bets_current_phase(D,E),E<2, !.
% 2308.0/3561.0=0.648132547037349
predict_distribution(A,B,[0.363310641347187,0.636689358652813]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5,player_previous_action(A,B,D,check),player_game_total_bets(A,B,E),E<1,player_preflop_play_frequency(B,F),F>0.4, !.
% 1776.0/2789.0=0.636787378988885
predict_distribution(A,B,[0.331480876103302,0.668519123896698]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5,player_previous_action(A,B,D,check),player_game_total_bets(A,B,E),E<1, !.
% 2044.0/3057.0=0.668629375204449
predict_distribution(A,B,[0.361645060348681,0.638354939651319]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5,player_previous_action(A,B,D,call_big), !.
% 1427.0/2235.0=0.638478747203579
predict_distribution(A,B,[0.467177607464179,0.532822392535821]) :- game_total_bets_current_phase(A,0),player_current_action(A,check),player_pot_ratio(A,B,C),C<0.5, !.
% 1598.0/2999.0=0.532844281427142
predict_distribution(A,B,[0.815676141257537,0.184323858742463]) :- game_total_bets_current_phase(A,0),player_current_action(A,check), !.
% 1893.0/2320.0=0.815948275862069
predict_distribution(A,B,[0.544842254519674,0.455157745480326]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_preflop_raise_frequency(B,D),D<0.1,player_current_action(A,fold),player_preflop_play_frequency(B,E),E>0.4, !.
% 1536.0/2819.0=0.54487406881873
predict_distribution(A,B,[0.418102269619907,0.581897730380093]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_preflop_raise_frequency(B,D),D<0.1,player_current_action(A,fold), !.
% 2127.0/3655.0=0.581942544459644
predict_distribution(A,B,[0.538526315789474,0.461473684210526]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_preflop_raise_frequency(B,D),D<0.1,player_preflop_play_frequency(B,E),E<0.4, !.
% 1278.0/2373.0=0.538558786346397
predict_distribution(A,B,[0.389038634321653,0.610961365678347]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_preflop_raise_frequency(B,D),D<0.1, !.
% 2039.0/3337.0=0.611027869343722
predict_distribution(A,B,[0.468761690983913,0.531238309016087]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_absolute_after(A,D),player_pot_ratio(A,D,E),E<0.5, !.
% 1419.0/2671.0=0.531261699737926
predict_distribution(A,B,[0.626151820974111,0.373848179025889]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_current_action(A,fold),player_preflop_play_frequency(B,D),D>0.4, !.
% 1426.0/2277.0=0.626262626262626
predict_distribution(A,B,[0.381163084702908,0.618836915297092]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_current_action(A,fold),player_preflop_raise_frequency(B,D),D<0.2, !.
% 1957.0/3162.0=0.618912080961417
predict_distribution(A,B,[0.465392934390771,0.534607065609229]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_current_action(A,fold), !.
% 1482.0/2772.0=0.534632034632035
predict_distribution(A,B,[0.410102437301307,0.589897562698693]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C),player_preflop_play_frequency(B,D),D>0.4, !.
% 1669.0/2829.0=0.589961117002474
predict_distribution(A,B,[0.613839285714286,0.386160714285714]) :- game_total_bets_current_phase(A,0),game_current_phase(A,preflop),player_relative_after(A,C), !.
% 2199.0/3582.0=0.613902847571189
predict_distribution(A,B,[0.993336246449639,0.0066637535503605]) :- game_total_bets_current_phase(A,0), !.
% 9092.0/9152.0=0.993444055944056
%was: predict_distribution(A,B,[0.999832971438116,0.000167028561884082]) :- player_current_action(A,check), !.
predict_distribution(A,B,[1.0,0.0]) :- player_current_action(A,check), !.
% 5985.0/5985.0=1.0
predict_distribution(A,B,[0.359347442680776,0.640652557319224]) :- game_current_phase(A,preflop),player_current_action(A,call),player_pot_ratio(A,B,C),C<0.2, !.
% 1452.0/2266.0=0.640776699029126
predict_distribution(A,B,[0.454948301329394,0.545051698670606]) :- game_current_phase(A,preflop),player_current_action(A,call),player_preflop_play_frequency(B,C),C>0.4, !.
% 1106.0/2029.0=0.545096106456382
predict_distribution(A,B,[0.569600620395502,0.430399379604498]) :- game_current_phase(A,preflop),player_current_action(A,call), !.
% 1468.0/2577.0=0.56965463717501
predict_distribution(A,B,[0.411304347826087,0.588695652173913]) :- game_current_phase(A,preflop),player_pot_odds(A,B,C),C>0.5,player_win_rate(A,B,D),D>0, !.
% 1353.0/2298.0=0.588772845953003
predict_distribution(A,B,[0.38454011741683,0.61545988258317]) :- game_current_phase(A,preflop),player_pot_odds(A,B,C),C>0.5, !.
% 1257.0/2042.0=0.615572967678746
predict_distribution(A,B,[0.564516129032258,0.435483870967742]) :- game_current_phase(A,preflop),player_playing_style_preflop(B,loose), !.
% 1189.0/2106.0=0.564577397910731
predict_distribution(A,B,[0.439119804400978,0.560880195599022]) :- game_current_phase(A,preflop), !.
% 1146.0/2043.0=0.560939794419971
predict_distribution(A,B,[0.328842315369261,0.671157684630739]) :- player_action_current_game(A,B,C,check),player_pot_committed(A,B,D),D<0.5,player_current_action(A,call),player_action_current_game(C,B,E,call_small), !.
% 1344.0/2002.0=0.671328671328671
predict_distribution(A,B,[0.310104529616725,0.689895470383275]) :- player_action_current_game(A,B,C,check),player_pot_committed(A,B,D),D<0.5,player_current_action(A,call), !.
% 1385.0/2007.0=0.690084703537618
predict_distribution(A,B,[0.224584555229717,0.775415444770283]) :- player_action_current_game(A,B,C,check),player_pot_committed(A,B,D),D<0.5,player_previous_action(A,B,E,check),player_pot_ratio(A,B,F),F<0.2, !.
% 3172.0/4090.0=0.775550122249389
predict_distribution(A,B,[0.318462096481683,0.681537903518317]) :- player_action_current_game(A,B,C,check),player_pot_committed(A,B,D),D<0.5, !.
% 1878.0/2755.0=0.681669691470054
predict_distribution(A,B,[0.269321633479581,0.730678366520419]) :- player_current_action(A,call), !.
% 2164.0/2961.0=0.730834177642688
predict_distribution(A,B,[0.34361893349311,0.65638106650689]).
% 2190.0/3336.0=0.656474820143885

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% MODEL USAGE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

prior_action_probability(GameID, ActionID, Player, Action, Round, Probability) :-
	% P(a|Dmin)	
	stored_prior_a_if_dmin(Round, Action, D_min),
	% P(Dstar|a)
	predict_distribution(ex(GameID, ActionID, _Outcome, Action, _Class),Player,[_,D_star]),
	% P(a|Dprior)	
	Probability is D_min*D_star/(1-D_star).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% HACK!!
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% temp hack for missing predicates
player(_,_,_,_,_,_,_) :- fail.
game_player_hole_card_1(_,_,_) :- fail.
game_player_hole_card_2(_,_,_) :- fail.
game_player_profit(_,_,_) :- fail.
board_maxsuit(_,_,_) :- fail.
board_maxseq(_,_,_) :- fail.
board_highcard(_,_,_) :- fail.
board_type(_,_,_) :- fail.
card_group(_,_,_) :- fail.
hand_type(_,_,_,_) :- fail.
hand_maxseq(_,_,_,_) :- fail.
hand_maxsuit(_,_,_,_) :- fail.
hand_strength(_,_,_,_) :- fail.
hand_effective_strength(_,_,_,_) :- fail.
hand_potential(_,_,_,_) :- fail.
num_bets(_,_,_) :- fail.
num_calls(_,_,_) :- fail.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% GAME TESTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% converted preprocessor assertions

% game_current_phase(+Example,-Phase).
game_current_phase(ex(GameID,ActionID,_,_,_),Phase) :-
	compute_game_current_phase(GameID,ActionID,Phase,_).

% game_remaining_players(+Example,-RemainingPlayers).
game_remaining_players(ex(GameID,ActionID,_,_,_),RemainingPlayers) :-
	compute_game_remaining_players(GameID,ActionID,RemainingPlayers).

% game_players_to_act_absolute(+Example,-PlayersToActAbs).
game_players_to_act_absolute(ex(GameID,ActionID,_,_,_),PlayersToActAbs):-
	compute_game_players_to_act_absolute(GameID,ActionID,PlayersToActAbs).

% game_players_to_act_relative(+Example,-PlayersToActRel).
game_players_to_act_relative(ex(GameID,ActionID,_,_,_),PlayersToActRel) :-
        compute_game_players_to_act_relative(GameID,ActionID,PlayersToActRel).

% game_total_bets(+Example,-TotalBets).
game_total_bets(ex(GameID,ActionID,_,_,_),TotalBets) :-
	compute_game_total_bets(GameID,ActionID,TotalBets).

% game_total_bets_current_phase(+Example,-TotalBetsPhase).
game_total_bets_current_phase(ex(GameID,ActionID,_,_,_),TotalBetsPhase) :-
	compute_game_total_bets_current_phase(GameID,ActionID,TotalBetsPhase).

fold_time(GameID,PlayerID,ActionID) :- 
	game_player_action(GameID,ActionID,PlayerID,folds,_,_).

%%%%% Implemenation for computing values for game tests

some_phase(preflop).
some_phase(flop).
some_phase(turn).
some_phase(river).

% find current phase in game i.e., preflop, flop, turn or river and starting id.
compute_game_current_phase(GameID,ActionID,Phase,PhaseStart):-
	findall(phase(Ph,A),(game_phase_start(GameID,Ph,A),A=<ActionID),L),
	find_phase_start(L,phase(preflop,0),phase(Phase,PhaseStart)).

find_phase_start([],L,L):-!.
find_phase_start([phase(Ph,Start)|R],phase(MP,Max),Out):-
	(Start > Max ->
		find_phase_start(R,phase(Ph,Start),Out)
	;
		find_phase_start(R,phase(MP,Max),Out)
	).

% find current round in game i.e., 1,2, 3 and starting id.
compute_game_current_round(GameID,ActionID,Phase,Round,RoundStart):-
	compute_game_current_phase(GameID,ActionID,Phase,_),
        findall(round(Rd,A),(game_round_start(GameID,Phase,Rd,A),A=<ActionID),L),
        find_round_start(L,round(0,0),round(Round,RoundStart)).

find_round_start([],L,L):-!.
find_round_start([round(Rd,Start)|R],round(MP,Max),Out):-
        (Start > Max ->
                find_round_start(R,round(Rd,Start),Out)
        ;
                find_round_start(R,round(MP,Max),Out)
        ).

% get player count at (just before) ActionID
compute_game_remaining_players(GameID,ActionID,PlayerCount) :-
	findall(P, (game_player_seat(GameID,P,_),player_alive(GameID,ActionID,P)), Players),
	length(Players,PlayerCount).

% returns the number of players that has position on player currently acting
compute_game_players_to_act_absolute(GameID,ActionID,PlayersToAct):-
        game_player_action(GameID,ActionID,PlayerID,_,_,_),
	findall(P,(game_player_seat(GameID,P,_),
                        P \= PlayerID,
			player_alive(GameID,ActionID,P),
			compute_player_absolute_after(GameID,ActionID,P)),L),
        length(L,PlayersToAct).

% returns the number of players that still need to act in this round after the player currently acting
compute_game_players_to_act_relative(GameID,ActionID,PlayersToAct):-
	game_player_action(GameID,ActionID,PlayerID,_,_,_),
	findall(P,(game_player_seat(GameID,P,_),
			P \= PlayerID,
			player_alive(GameID,ActionID,P),
			compute_player_relative_after(GameID,ActionID,P)),L),
	length(L,PlayersToAct).

% returns the total number of bets made by any of the players in the game
compute_game_total_bets(GameID,ActionID,NumBets):-
	findall(A,((game_player_action(GameID,A,_,bets(_),_,_);
			game_player_action(GameID,A,_,raises(_,_),_,_)),
			%;game_player_action(GameID,A,_,allin(_),_,_)),
			A < ActionID), L),
	length(L,NumBets).

% returns the number of bets made by any of the players in this phase of game
compute_game_total_bets_current_phase(GameID,ActionID,NumBets):-
	compute_game_current_phase(GameID,ActionID,_,PhaseStart),
	findall(A,((game_player_action(GameID,A,_,bets(_),_,_);
			game_player_action(GameID,A,_,raises(_,_),_,_)),
			%;game_player_action(GameID,A,_,allin(_),_,_)),
			A < ActionID, A >= PhaseStart), L),
	length(L,NumBets).

% returns the total number of bets made by a player in the game
compute_game_player_total_bets(GameID,ActionID,NumBets):-
	game_player_action(GameID,ActionID,PlayerID,_,_,_),
        findall(A,((game_player_action(GameID,A,PlayerID,bets(_),_,_);
			game_player_action(GameID,A,PlayerID,raises(_,_),_,_)),
			%;game_player_action(GameID,A,PlayerID,allin(_),_,_)),
			A < ActionID), L),
        length(L,NumBets).

% calculate the money invested so far in the game by a player
compute_game_player_investment(GameID,ActionID,InvestedMoney):-
	game_player_action(GameID,ActionID,PlayerID,_,_,_),
        findall(M,((game_player_action(GameID,A,PlayerID,Action,_,_),(
                        Action = calls(M) ; Action = bets(M) ;
                        Action = raises(_,M) ; Action = allin(M)),
                    A < ActionID)),L),
        sumlist(L,0,M1),!,
        (
                game_player_small_blind(GameID,PlayerID) -> game_stakes(GameID,M2,_);
                game_player_big_blind(GameID,PlayerID) -> game_stakes(GameID,_,M2); % TODO: ante
                M2 is 0
        ),!,
        InvestedMoney is M1 + M2.

% calculate the profit of player in this game.
compute_game_player_profit(GameID,PlayerID,Profit):-
	(game_player_profit(GameID,PlayerID,Profit) -> 
		true
		;
        	findall(M,((game_player_action(GameID,_,PlayerID,Action,_,_),(
                   	     	Action = calls(M) ; Action = bets(M) ;
                        	Action = raises(_,M) ; Action = allin(M))
                    	  )),L),
        	sumlist(L,0,M1),!,
        	(
                	game_player_small_blind(GameID,PlayerID) -> game_stakes(GameID,M2,_);
                	game_player_big_blind(GameID,PlayerID) -> game_stakes(GameID,_,M2); % TODO: ante
                	M2 is 0
        	),!,
        	Profit is 0 - M1 - M2
	).

% calculate the win rate at the current session of the player.
compute_game_player_win_rate(GameID,ActionID,WinRate):-
	game_player_action(GameID,ActionID,PlayerID,_,_,_),
        findall(wr(P,BigBlind),(game_stakes(GID,_,BigBlind),
			compute_game_player_profit(GID,PlayerID,P),
			game_session(GID,Session),
			game_session(GameID,Session),
			GID < GameID),L),
        sum_profit(L,0,WR),
        WinRate is WR.

sum_profit([],Result,Result):-!.
sum_profit([wr(X,Y)|L],Sum,Result):-
        NewSum is Sum + (X/Y),
        sum_profit(L,NewSum,Result).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% PLAYER TESTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%% Implemenation for various player tests

% returns true if player still alive prior to action ActionID
player_alive(GameID,ActionID,PlayerID) :-
        (fold_time(GameID,PlayerID,T) -> ActionID =< T; true).

% check if active player exists with earlier position
compute_player_absolute_before(GameID,ActionID,PlayerID):-
        game_player_action(GameID,ActionID,ThisPlayer,_,_,_),
        game_player_seat(GameID,ThisPlayer,ThisPlayerSeat),
        game_player_seat(GameID,PlayerID,Seat),
        Seat < ThisPlayerSeat.

% check if active player exists with later position
compute_player_absolute_after(GameID,ActionID,PlayerID):-
        game_player_action(GameID,ActionID,ThisPlayer,_,_,_),
        game_player_seat(GameID,ThisPlayer,ThisPlayerSeat),
        game_player_seat(GameID,PlayerID,Seat),
        Seat > ThisPlayerSeat.

% check if active player exists with relative earlier position (has already acted)
compute_player_relative_before(GameID,ActionID,PlayerID):-
	game_player_action(GameID,ActionID,ThisPlayer,_,_,_),
        compute_game_current_phase(GameID,ActionID,Phase,PhaseStart),
        findall(BetID,(game_player_action(GameID,BetID,_,BetAction,_,_),
                        (BetAction = bets(_);BetAction = raises(_,_)), %;BetAction = allin(_)),
                        BetID >= PhaseStart, BetID < ActionID),BetList),
        find_latest_bet(BetList,0,LatestBetID),!,
	(LatestBetID>0 ->
                game_player_action(GameID,PlayerActionID,PlayerID,_,_,_),
                compute_game_current_phase(GameID,PlayerActionID,Phase,PhaseStart),
                PlayerActionID>=LatestBetID,PlayerActionID<ActionID
        ;
                (Phase = preflop ->
			(  
				game_player_big_blind(GameID,ThisPlayer) -> true;
				game_player_big_blind(GameID,PlayerID) -> fail;
				game_player_small_blind(GameID,ThisPlayer) -> true;
				game_player_small_blind(GameID,PlayerID) -> fail;
				player_absolute_before(ex(GameID,ActionID,_,_,_),PlayerID)
				%wln([game(GameID),action(ActionID),player(PlayerID),'before',thisplayer(ThisPlayer)])
                        );
                        player_absolute_before(ex(GameID,ActionID,_,_,_),PlayerID)
                )
        ).

find_latest_bet([],M,M):-!.
find_latest_bet([b(H,P)|R],b(M,MP),MaxOut):-
	(H>M->
		find_latest_bet(R,b(H,P),MaxOut)
	;
		find_latest_bet(R,b(M,MP),MaxOut)
	).

% check if active player exists with relative later position (still to act)
compute_player_relative_after(GameID,ActionID,PlayerID):-
	\+(compute_player_relative_before(GameID,ActionID,PlayerID)).

%%%%% Tests on outcome

game_result(noplay).
game_result(win).
game_result(lose).
game_result(cards(_,_)).

% player_result(+Example,-ResultClass).
% returns the class of the current result for given example
player_result(ex(_,_,Outcome,_,_),Outcome).

% more detailed representation of results/outcomes

some_outcome(win).
some_outcome(lose).
some_outcome(cards(X,Y)) :-
        some_card(X),
        some_card(Y),
        X @> Y.

some_card(card(Rank,Suit)) :-
        member(Rank,[0,1,2,3,4,5,6,7,8,9,10,11,12]),
        member(Suit,[0,1,2,3]).

% game_player_outcome(+GameID,+PlayerID,-Outcome)
% find real outcome for player in a specific game.
game_player_outcome(GameID,PlayerID,Outcome) :-
	((game_player_hole_card_1(GameID,PlayerID,C1),game_player_hole_card_2(GameID,PlayerID,C2))->
		%wln([GameID,PlayerID,cards]),
        	(C1 @> C2 ->
        		Outcome = cards(C1,C2) ;
        		Outcome = cards(C2,C1)
		)
        	;
        	((game_player_profit(GameID,PlayerID,Profit),Profit > 0) ->
			%wln([GameID,PlayerID,win]),
			Outcome = win
			;
			((game_player_profit(GameID,PlayerID,Profit),Profit < 0) ->
				%wln([GameID,PlayerID,lose]),
				Outcome = lose
				;
				%wln([GameID,PlayerID,noplay]),
				Outcome = noplay
			)
		)
	).
	
%%%%% Tests specific for hole cards

player_hole_cards(ex(_,_,Outcome,_,_),Rank1,Rank2) :-
	ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
        (R1 > R2 -> Rank1 is R1,Rank2 is R2 ; Rank1 is R2,Rank2 is R1).

player_holds_pair(ex(_,_,Outcome,_,_)) :-
        ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
        R1 =:= R2.

player_holds_suited_cards(ex(_,_,Outcome,_,_)) :-
        ground(Outcome),
        Outcome = cards(card(_,S1),card(_,S2)),
        S1 =:= S2.

player_holds_connecting_cards(ex(_,_,Outcome,_,_)) :-
        ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
        Gap is R1 - R2,        
	(Gap =:= 1; Gap =:= -1).

player_gap_cards(ex(_,_,Outcome,_,_),Gap) :-
        ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
        Gap is abs(R1 - R2).

player_holds_num_face_cards(ex(_,_,Outcome,_,_),Num) :-
        ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
	(R1 > 8, R2 > 8 -> 
		Num is 2;
		((R1 > 8;R2 > 8) -> 
			Num is 1;
			Num is 0
		)
	).

player_holds_high_card(ex(_,_,Outcome,_,_),Value) :-
	ground(Outcome),
	Outcome = cards(card(R1,_),card(R2,_)),
	( R1 > R2 -> Value = R1 ; Value = R2 ).

player_holds_low_card(ex(_,_,Outcome,_,_),Value) :-
        ground(Outcome),
        Outcome = cards(card(R1,_),card(R2,_)),
        ( R1 < R2 -> Value = R1 ; Value = R2 ).

%% value of holecards is integer between 1 and 9, with 1 best and 9 lowest of value for holecards
player_value_hole_cards(ex(_,_,Outcome,_,_),Value) :-
        ground(Outcome),
        Outcome = cards(card(R1,S1),card(R2,S2)),
        card_group(Value,card(R1,S1),card(R2,S2)).

player_sum_hole_cards(ex(_,_,Outcome,_,_),Sum) :-
        ground(Outcome),
        Outcome = cards(card(R1,_S1),card(R2,_S2)),
	Sum is R1 + R2.

%%%%% Postflop card tests including monte-carlo simulation tests

card_type(highcard).
card_type(pair). 
card_type(lowpair).
card_type(toppair).
card_type(twopair).
card_type(threeofakind).
card_type(straight).
card_type(flush).
card_type(fullhouse).
card_type(fourofakind).
card_type(straightflush).

player_holds(ex(GameID,ActionID,_,_,Class),HandType):-
	hand_type(GameID,ActionID,Class,HandType).

%% value of postflop card combination lies between 1 and 10, with 1 lowest and 10 best combination.
player_holds_value(ex(GameID,ActionID,_,_,Class),HandTypeInt):-
	hand_type(GameID,ActionID,Class,HandType),
	handtype_to_int(HandType,HandTypeInt).

handtype_to_int(straightflush,10):-!.
handtype_to_int(fourofakind,9):-!.
handtype_to_int(fullhouse,8):-!.
handtype_to_int(flush,7):-!.
handtype_to_int(straight,6):-!.
handtype_to_int(threeofakind,5):-!.
handtype_to_int(twopair,4):-!.
handtype_to_int(toppair,3):-!.
handtype_to_int(lowpair,2):-!.
handtype_to_int(highcard,1):-!.

player_holds_straight_draw(ex(GameID,ActionID,_,_,Class)):-
        hand_maxseq(GameID,ActionID,Class,MaxSeq),
        MaxSeq =:= 4.

player_holds_flush_draw(ex(GameID,ActionID,_,_,Class)):-
        hand_maxsuit(GameID,ActionID,Class,MaxSuit),
        MaxSuit =:= 4.

player_hand_strength(ex(GameID,ActionID,_,_,Class),HandStrength):-
	hand_strength(GameID,ActionID,Class,HandStrength).

player_hand_potential(ex(GameID,ActionID,_,_,Class),HandPotential):-
        hand_potential(GameID,ActionID,Class,HandPotential).

player_hand_effective_strength(ex(GameID,ActionID,_,_,Class),EffectiveHandStrength):-
        hand_effective_strength(GameID,ActionID,Class,EffectiveHandStrength).

%%%%% Tests that can introduce new opponents

% active_player(+Example,-PlayerID)
% check if a player is still active at given action id
active_player(ex(GameID,ActionID,_,_,_),PlayerID):-
        game_player_action(GameID,ActionID,ThisPlayer,_,_,_),
	game_player_seat(GameID,PlayerID,_),
	ThisPlayer \= PlayerID,
        player_alive(GameID,ActionID,PlayerID).

% player_absolute_before(+Example,-PlayerID)
% check if player is has earlier position than current player (at given action id).
player_absolute_before(ex(GameID,ActionID,_,_,_),PlayerID):-
        active_player(ex(GameID,ActionID,_,_,_),PlayerID),
        compute_player_absolute_before(GameID,ActionID,PlayerID).

% player_absolute_after(+Example,-PlayerID)
% check if player has later position than current player.
player_absolute_after(ex(GameID,ActionID,_,_,_),PlayerID):-
        active_player(ex(GameID,ActionID,_,_,_),PlayerID),
        compute_player_absolute_after(GameID,ActionID,PlayerID).

% player_relative_before(+Example,-PlayerID)
% check if player is has already acted before current player.
player_relative_before(ex(GameID,ActionID,_,_,_),PlayerID):-
	active_player(ex(GameID,ActionID,_,_,_),PlayerID),
	compute_player_relative_before(GameID,ActionID,PlayerID).

% player_relative_after(+Example,-PlayerID)
% check if player is must still act after modelled player.
player_relative_after(ex(GameID,ActionID,_,_,_),PlayerID):-
        active_player(ex(GameID,ActionID,_,_,_),PlayerID),
        compute_player_relative_after(GameID,ActionID,PlayerID).

%TODO: we can assert player names from output_opponent_player file
some_player(player_ace1).
some_player(player_ace2).
some_player(player_ace3).
some_player(player_ace4).

player_name(ex(GameID,ActionID,_,_,_),PlayerID):-
	active_player(ex(GameID,ActionID,_,_,_),PlayerID).

%%%%% Tests on player actions

% action class
action_class(fold).
action_class(check).
action_class(call).
action_class(bet).
%action_class(raise).
%action_class(allin).

get_action_class(folds,fold).
get_action_class(checks,check).
get_action_class(calls(_),call).
get_action_class(bets(_),bet).
get_action_class(raises(_,_),bet).
%get_action_class(allin(_),allin).

% simple action types of earlier actions in games for active players (therefore no fold), used in action tests
simple_action_type(check).
simple_action_type(call).
simple_action_type(bet).

get_action_type(checks,check).
get_action_type(calls(_),call).
get_action_type(bets(_),bet).
get_action_type(raises(_,_),bet).

% simple categorization
simple_action_type(Action,Type):-
        get_action_type(Action,Type).

% detailed action types of earlier actions in games for active players (therefore no fold), used in action tests
detailed_action_type(check).
detailed_action_type(call_small).
detailed_action_type(call_big).
detailed_action_type(bet_small).
detailed_action_type(bet_big).
detailed_action_type(raise).

% detailed categorizations of earlier  actions, in particular bets and raises (no-limit holdem
detailed_action_type(A,Type) :-
	action(Action,PotTotal) = A,
        ( Action = checks ->
                Type = check
                ;
                ( Action = calls(Amount) ->
                        ( Amount =< 0.5 * PotTotal ->
                                Type = call_small
                                ;
                                Type = call_big
                        )
                        ;
                        ( Action = bets(Amount) ->
                                ( Amount =< 0.5 * PotTotal ->
                                        Type = bet_small
                                        ;
                                        Type = bet_big
                                )
                                ;
                                ((Action = raises(_,_))-> %; Action = allin(_)) ->
                                        Type = raise
                                        ;
					%wln([detailed_action_type(A),' fails']),
					fail,!
                                )
                        )
                )
        ).

find_max_action([],M,M,T,T,P,P):-!.
find_max_action([action(AID,Action,PotTotal)|R],Max,ActionOut,MType,TypeOut,Pot,PotOut):-
        (AID>Max->
                find_max_action(R,AID,ActionOut,Action,TypeOut,PotTotal,PotOut)
                ;
                find_max_action(R,Max,ActionOut,MType,TypeOut,Pot,PotOut)
        ).

% player_current_action(+Example,-ActionClass).
% returns the class of the current action for given example
player_current_action(ex(_,_,_,Action,_),Action).
%player_current_action(ex(GameID,ActionID,_,Action,_)).

% find an earlier action for specific active player, before to this action id.
% player_action_current_game(+Example,+PlayerID,-NewExample,ActionType).
player_action_current_game(ex(GameID,ActionID,_,_,_),PlayerID,ex(GameID,AID,_,_,_),ActionType):-
	active_player(ex(GameID,ActionID,_,_,_),PlayerID),
	game_player_action(GameID,AID,PlayerID,Action,_,PotTotal),
        AID<ActionID,
	simple_action_type(Action,ActionType).
        %detailed_action_type(action(Action,PotTotal),ActionType).

% find the previous action for specific active player before to this action id.
% player_previous_action(+Example,+PlayerID,-NewExample,ActionType).
player_previous_action(ex(GameID,ActionID,_,_,_),PlayerID,ex(GameID,AID,_,_,_),ActionType):-
        active_player(ex(GameID,ActionID,_,_,_),PlayerID),
	findall(action(AID,Action,PotTotal),(game_player_action(GameID,AID,PlayerID,Action,_,PotTotal), AID<ActionID),Actions),
        find_max_action(Actions,0,Latest,_,LType,_,PotTotal),
        (Latest > 0 ->
                AID = Latest,
                simple_action_type(LType,ActionType)
                %detailed_action_type(action(LType,PotTotal),ActionType)
        ;
                fail
        ).

player_num_actions_this_phase(ex(GameID,ActionID,_,_,_),PlayerID,NumBets):-
	findall(AID,(game_player_action(GameID,AID,PlayerID,_,_,_), 
			AID<ActionID,
			game_current_phase(GameID,ActionID,Phase),
			game_current_phase(GameID,AID,Phase)),Actions),
	length(Actions,NumBets).

%%% Misc tests

% check if player is blind this game
player_is_blind(ex(GameID,_,_,_,_),PlayerID):-
        (game_player_small_blind(GameID,PlayerID);game_player_big_blind(GameID,PlayerID)).

% check if player is small blind this game
player_is_small_blind(ex(GameID,_,_,_,_),PlayerID):-
        game_player_small_blind(GameID,PlayerID).

% check if player is big blind this game
player_is_big_blind(ex(GameID,_,_,_,_),PlayerID):-
        game_player_big_blind(GameID,PlayerID).

% find the zone (stack / summed blinds) for player this game
player_in_zone(ex(GameID,_,_,_,_),PlayerID,Zone):-
        game_player_stack(GameID,PlayerID,Stack),
        game_stakes(GameID,SB,BB),
        M is Stack / (SB + BB),
        (M >= 20 ->
                Zone = green
        ;
                (M >= 10 ->
                        Zone = yellow
                ;
                        (M >= 5 ->
                                Zone = orange
                        ;
                                Zone = red
                        )
                )
        ).

% find the pot odds for a specific player in this game
player_pot_odds(ex(GameID,ActionID,_,_,_),PlayerID,PotOdds):-
	game_player_action(GameID,ActionID,PlayerID,_,ToCall,PotTotal),
	PotOdds is ToCall / PotTotal.

% find the amount invested by player previous to this action
player_pot_ratio(ex(GameID,ActionID,_,_,_),PlayerID,PotRatio):-
	game_player_action(GameID,ActionID,_,_,_,PotTotal),
        findall(IM,(game_player_action(GameID,AID,PlayerID,_,_,_),
                        game_player_investment(GameID,AID,IM),
			AID < ActionID),L),
        max_list(L,InvestedMoney),
        PotRatio is InvestedMoney / PotTotal.

%findall(f(GID,AID,C),(ex(GID,AID,Oc,Ac,C),Oc=lose,Ac=fold,player_pot_committed(ex(GID,AID,_,_,C),player_ace,D),D>=0.35),L).
%findall(f(GID,AID,C),(ex(GID,AID,Oc,Ac,C),game_player_action(GID,AID,PID,_,_,_),PID=player_ace,Oc\=lose,Ac=fold),L),length(L,Len).

% find the amount invested compared to begin stack previous to this action
player_pot_committed(ex(GameID,ActionID,_,_,_Class),PlayerID,PotCommit):-
        game_player_stack(GameID,PlayerID,Stack),
        findall(IM,(game_player_action(GameID,AID,PlayerID,_,_,_),
                        game_player_investment(GameID,AID,IM),
			AID < ActionID),L),
        max_list(L,InvestedMoney),
        PotCommit is InvestedMoney / Stack.

% this should be per game, similar to player_in_zone
player_win_rate(ex(GameID,_,_,_,_),PlayerID,WinRate):-
	game_player_action(GameID,AID,PlayerID,_,_,_),!,
	once(game_player_win_rate(GameID,AID,WinRate)).

% find the number of bets for specific opponent previous to this action
player_game_total_bets(ex(GameID,ActionID,_,_,_),PlayerID,NumBets):-
	findall(B,(game_player_action(GameID,AID,PlayerID,_,_,_),
			game_player_total_bets(GameID,AID,B),
			AID < ActionID),L),
	max_list(L,NumBets).

player_num_actions_this_phase(ex(GameID,ActionID,_,_,_),PlayerID,NumActions):-
        findall(AID,(game_player_action(GameID,AID,PlayerID,_,_,_),
			game_current_phase(GameID,ActionID,Phase),
			game_current_phase(GameID,AID,Phase),
                        AID < ActionID),L),
	length(L,NumActions).

% if player has played at least NumGames then return player aggression 
player_aggression(ex(GameID,ActionID,_,_,_),PlayerID,NumGames,Aggression) :-
	( NumGames == 0 ->
		findall(A,(game_player_action(GameID,AID,PlayerID,A,_,_),
				AID<ActionID),
			ActionList)
		;
		findall(A,(game_player_action(GID,_,PlayerID,A,_,_),
				game_session(GameID,Session),
				game_session(GID,Session),
				GID>=(GameID-NumGames),
				GID<GameID),
			ActionList)
	),
	num_bets(ActionList,0,NumBets),
	num_calls(ActionList,0,NumCalls),
	(NumCalls > 0 ->
		Aggression is NumBets / NumCalls
		;	
		Aggression is NumBets
	).

% calculate stack ratio between two players.
player_stack_ratio(ex(GameID,ActionID,_,_,_),PlayerID,Ratio) :-
        game_player_action(GameID,ActionID,ThisPlayer,_,_,_),
        game_player_stack(GameID,ThisPlayer,ThisPlayerStack),
        game_player_stack(GameID,PlayerID,Stack),
        Ratio is ThisPlayerStack / Stack.

%%% A priori calculated opponent data (characterizing player)

player_playing_style_preflop(PlayerID,Type):-
        (player(PlayerID,Icon,_,_,_,_,_) ->
		((Icon=2;Icon=8;Icon=10;Icon=14) -> Type = loose;Type=tight)
		;fail
	).

player_playing_style_postflop(PlayerID,Type) :-
        (player(PlayerID,Icon,_,_,_,_,_) ->
		((Icon=2;Icon=5;Icon=6;Icon=12;Icon=13) -> Type = passive;Type = aggressive)
		;fail
	).

player_preflop_play_frequency(PlayerID,PreflopPlayFrequency):-
        player(PlayerID,_,PreflopPlayFrequency,_,_,_,_).

player_preflop_raise_frequency(PlayerID,PreflopRaiseFrequency):-
        player(PlayerID,_,_,PreflopRaiseFrequency,_,_,_).

player_aggression(PlayerID,Aggression):-
        player(PlayerID,_,_,_,Aggression,_,_).

player_saw_showdown(PlayerID,SawShowdownFrequency):-
        player(PlayerID,_,_,_,_,SawShowdownFrequency,_).

player_won_showdown(PlayerID,WonShowdownFrequency):-
        player(PlayerID,_,_,_,_,_,WonShowdownFrequency).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% BOARD TESTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

board_type(ex(GameID,ActionID,_,_,_),BoardType):-
	board_type(GameID,ActionID,BoardType).

high_card_on_board(ex(GameID,ActionID,_,_,_),HighCard):-
	board_highcard(GameID,ActionID,HighCard).

straight_draw_on_board(ex(GameID,ActionID,_,_,_)):-
	board_maxseq(GameID,ActionID,MaxSeq),
        MaxSeq >= 2.

flush_draw_on_board(ex(GameID,ActionID,_,_,_)):-
        board_maxsuit(GameID,ActionID,MaxSuit),
	MaxSuit >= 2.

